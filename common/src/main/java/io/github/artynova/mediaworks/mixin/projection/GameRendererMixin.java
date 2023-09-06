package io.github.artynova.mediaworks.mixin.projection;

import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void cancelHandRendering(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V"))
    private void applyShader(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            AstralProjectionClient.renderShader(tickDelta);
        }
    }

    @Inject(method = "onResized", at = @At(value = "TAIL"))
    private void onResized(int width, int height, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            AstralProjectionClient.setupShaderDimensions(width, height);
        }
    }
}
