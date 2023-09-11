package io.github.artynova.mediaworks.client.projection;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.artynova.mediaworks.client.render.RenderHelper;
import io.github.artynova.mediaworks.client.render.ShaderLoader;
import io.github.artynova.mediaworks.networking.projection.SyncAstralPositionC2SMsg;
import io.github.artynova.mediaworks.networking.projection.CastAstralIotaC2SMsg;
import io.github.artynova.mediaworks.networking.projection.EndProjectionC2SMsg;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.logic.projection.AstralPosition;
import io.github.artynova.mediaworks.sound.AstralAmbienceLoop;
import io.github.artynova.mediaworks.util.HexHelpers;
import io.github.artynova.mediaworks.util.MathHelpers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.function.TriFunction;

@Environment(EnvType.CLIENT)
public class AstralProjectionClient {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final TriFunction<Float, Float, Float, Float> INTERPOLATION_DELTA_FUNCTION = MathHelpers.slowdownInterpolationProgressWithCoeff(10);
    private static final int CLOSE_OVERLAY_COLOR = 0x00_888888;
    private static final int FAR_OVERLAY_COLOR = 0xEE_111111;
    private static final float[] FOG_COMPONENTS = new float[]{0.588f, 0.588f, 0.588f};
    private static final float FOG_THICKENING_GRADIENT_BLOCKS = 1.0f;
    private static final float FAR_FOG_RADIUS = 16f;
    private static AstralCameraEntity astralCamera;
    private static AstralAmbienceLoop ambienceLoop;

    /**
     * Ticks the astral camera.
     */
    public static void handlePostTick(MinecraftClient client) {
        if (client.isPaused()) return;
        if (!isDissociated()) return;

        double prevX = astralCamera.getX();
        double prevY = astralCamera.getY();
        double prevZ = astralCamera.getZ();

        astralCamera.tick();

        astralCamera.prevX = prevX;
        astralCamera.prevY = prevY;
        astralCamera.prevZ = prevZ;

        syncToServer();
    }

    /**
     * Removes the stored astral camera and shader.
     */
    public static void handleQuit(ClientPlayerEntity player) {
        if (!isDissociated()) return;
        astralCamera = null;
        CLIENT.getSoundManager().stop(ambienceLoop);
        ambienceLoop = null;
    }

    /**
     * Renders the player's astral body.
     */
    public static void renderAstralBody(MatrixStack matrixStack, WorldRenderer worldRenderer, Camera camera, float tickDelta, VertexConsumerProvider consumers) {
        if (camera.isThirdPerson()) {
            RenderHelper.renderPlayerEntity(astralCamera, matrixStack, worldRenderer, camera, tickDelta, consumers);
        }
    }

    public static void applyFogPosition(float viewDistance, BackgroundRenderer.FogType fogType) {
        float fogRadius = Math.min(viewDistance, (float) HexHelpers.getAmbitRadius(MinecraftClient.getInstance().player));
        ClientPlayerEntity player = CLIENT.player;
        double ambit = HexHelpers.getAmbitRadius(player);
        float squaredDist = (float) astralCamera.getPos().squaredDistanceTo(player.getPos());
        float squaredAmbit = (float) (ambit * ambit);
        if (squaredDist > squaredAmbit) {
            float squaredFullFogDist = MathHelper.square((float) ambit + FOG_THICKENING_GRADIENT_BLOCKS);
            float squaredDistClamped = MathHelper.clamp(squaredDist, squaredAmbit, squaredFullFogDist);
            // if the player has passed the natural ambit, rapidly approach fog radius towards FOG_RADIUS_BEYOND_AMBIT depending on how close to the ambit limit the player is
            fogRadius = MathHelper.lerp(INTERPOLATION_DELTA_FUNCTION.apply(squaredDistClamped, squaredAmbit, squaredFullFogDist), fogRadius, FAR_FOG_RADIUS);
        }
        float fogStart, fogEnd;
        if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
            fogStart = 0.0F;
            fogEnd = fogRadius * 0.8F;
        } else {
            fogStart = fogRadius * 0.25F;
            fogEnd = fogRadius;
        }
        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
        RenderSystem.setShaderFogShape(FogShape.SPHERE);
    }

    public static void applyBaseColor() {
        RenderSystem.clearColor(FOG_COMPONENTS[0], FOG_COMPONENTS[1], FOG_COMPONENTS[2], 0.0f);
    }

    public static void applyFogColor() {
        RenderSystem.setShaderFogColor(FOG_COMPONENTS[0], FOG_COMPONENTS[1], FOG_COMPONENTS[2]);
    }

    public static void renderShader(float tickDelta) {
        ShaderLoader.getShader().render(tickDelta);
    }

    public static void renderOverlay(MatrixStack matrixStack) {
        int scaledWidth = CLIENT.getWindow().getScaledWidth();
        int scaledHeight = CLIENT.getWindow().getScaledHeight();
        MinecraftClient client = MinecraftClient.getInstance();
        float squaredDistance = (float) astralCamera.getPos().squaredDistanceTo(client.player.getPos());
        int color = RenderHelper.interpolateColor(squaredDistance, 0, (float) Math.pow(HexHelpers.getAmbitRadius(client.player), 2), CLOSE_OVERLAY_COLOR, FAR_OVERLAY_COLOR, INTERPOLATION_DELTA_FUNCTION);
        DrawableHelper.fill(matrixStack, 0, 0, scaledWidth, scaledHeight, color);
    }

    public static void startProjection() {
        ClientPlayerEntity player = CLIENT.player;
        ambienceLoop = new AstralAmbienceLoop(player);
        astralCamera = new AstralCameraEntity(CLIENT, CLIENT.world);
        astralCamera.input = new KeyboardInput(CLIENT.options);
        CLIENT.getSoundManager().play(ambienceLoop);
        clearInputActivities(player.input);
        player.input = new Input();
        CLIENT.setCameraEntity(astralCamera);
        MutableText text = Text.translatable("mediaworks.messages.projection", CLIENT.options.inventoryKey.getBoundKeyLocalizedText());
        CLIENT.inGameHud.setOverlayMessage(text, false);
        CLIENT.getNarratorManager().narrate(text);
    }

    private static void clearInputActivities(Input input) {
        input.jumping = false;
        input.sneaking = false;
        input.movementForward = 0;
        input.movementSideways = 0;
    }

    public static void tryCast() {
        MediaworksNetworking.sendToServer(new CastAstralIotaC2SMsg());
    }

    public static void endProjection() {
        if (!isDissociated()) return;
        CLIENT.setCameraEntity(CLIENT.player);
        CLIENT.player.input = new KeyboardInput(CLIENT.options);
        astralCamera = null;
        CLIENT.getSoundManager().stop(ambienceLoop);
        ambienceLoop = null;
    }

    public static boolean isDissociated() {
        return astralCamera != null;
    }

    /**
     * Should not be called on a non-dissociated client.
     *
     * @return coordinates of the astral camera.
     */
    private static AstralPosition getAstralPosition() {
        assert isDissociated(); // should NOT be called on a non-dissociated client
        return new AstralPosition(astralCamera.getPos(), astralCamera.getYaw(), astralCamera.getPitch());
    }

    public static void syncToServer() {
        if (!isDissociated()) return;
        MediaworksNetworking.sendToServer(new SyncAstralPositionC2SMsg(getAstralPosition()));
    }

    public static void syncFromServer(AstralPosition incoming) {
        if (!isDissociated()) startProjection();
        astralCamera.updatePositionAndAngles(incoming.coordinates().getX(), incoming.coordinates().getY(), incoming.coordinates().getZ(), incoming.yaw(), incoming.pitch());
    }

    public static void initiateEarlyEnd() {
        MediaworksNetworking.sendToServer(new EndProjectionC2SMsg());
    }

    public static AstralCameraEntity getAstralCamera() {
        return astralCamera;
    }
}
