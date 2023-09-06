package io.github.artynova.mediaworks.mixin.projection;

import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void overwriteFogPosition(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) AstralProjectionClient.applyFogPosition(viewDistance, fogType);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private static void overwriteBaseColor(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) AstralProjectionClient.applyBaseColor();
    }

    @Inject(method = "setFogBlack", at = @At("TAIL"))
    private static void overwriteFogColor(CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) AstralProjectionClient.applyFogColor();
    }
}
