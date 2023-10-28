package io.github.artynova.mediaworks.mixin.macula;

import io.github.artynova.mediaworks.client.macula.MaculaClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Using a mixin because Macula is a separate kind of hud that should render
 * above normal hud.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V", shift = At.Shift.AFTER))
    private void renderMacula(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        MaculaClient.render(new MatrixStack(), tickDelta);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickMacula(CallbackInfo ci) {
        MaculaClient.tick();
    }

    @Inject(method = "onResized", at = @At("TAIL"))
    private void notifyAboutResize(int width, int height, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world != null) MaculaClient.sendDimensions();
    }
}
