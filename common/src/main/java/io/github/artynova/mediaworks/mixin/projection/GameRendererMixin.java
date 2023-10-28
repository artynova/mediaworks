package io.github.artynova.mediaworks.mixin.projection;

import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.client.projection.ShaderLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract MinecraftClient getClient();

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void cancelHandRendering(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F", ordinal = 0))
    private void applyOverlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            AstralProjectionClient.renderOverlay(new MatrixStack());
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void applyShader(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            AstralProjectionClient.renderShader(tickDelta);
            getClient().getFramebuffer().beginWrite(true); // so that projection shader doesn't kill menu rendering
        }
    }

    @Inject(method = "onResized", at = @At(value = "TAIL"))
    private void onResized(int width, int height, CallbackInfo ci) {
        ShaderLoader.setupShaderDimensions(width, height);
    }
}
