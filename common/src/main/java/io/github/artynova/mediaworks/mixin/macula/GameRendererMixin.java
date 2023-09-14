package io.github.artynova.mediaworks.mixin.macula;

import io.github.artynova.mediaworks.client.macula.MaculaClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Using a mixin because Macula is a separate kind of hud that should render
 * above normal hud.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderMacula(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack, MatrixStack matrixStack2) {
        MaculaClient.render(matrixStack2);
    }

    @Inject(method = "onResized", at = @At("TAIL"))
    private void notifyAboutResize(int width, int height, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world != null) MaculaClient.sendDimensions();
    }
}
