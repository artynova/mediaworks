package io.github.artynova.mediaworks.mixin.projection;

import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Hotbar hiding in particular is heavily based on that in Spirit Walker.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private int hotbarTime = 0;

    @Inject(method = "getCameraPlayer", at = @At("HEAD"), cancellable = true)
    private void injectPlayerBody(CallbackInfoReturnable<PlayerEntity> cir) {
        if (AstralProjectionClient.isDissociated()) cir.setReturnValue(MinecraftClient.getInstance().player);
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void updateHotbarOffset(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) hotbarTime++;
        else hotbarTime--;
        hotbarTime = MathHelper.clamp(hotbarTime, 0, 24);
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), index = 2)
    private int modifyBaseY(int y) {
        return y + hotbarTime;
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 1)
    private int modifyItemY(int y) {
        return y + hotbarTime;
    }
}
