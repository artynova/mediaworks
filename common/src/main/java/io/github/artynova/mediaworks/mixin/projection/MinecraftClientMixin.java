package io.github.artynova.mediaworks.mixin.projection;

import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), cancellable = true)
    private void cancelAttack(CallbackInfoReturnable<Boolean> cir) {
        if (AstralProjectionClient.isDissociated()) cir.setReturnValue(true);
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void redirectUse(CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            AstralProjectionClient.tryCast(); // casting when interaction key is pressed
            ci.cancel();
        }
    }

    @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
    private void cancelItemPick(CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) ci.cancel();
    }

    @Inject(method = "handleBlockBreaking", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;attackCooldown:I", opcode = Opcodes.GETFIELD), cancellable = true)
    private void cancelBlockBreak(boolean breaking, CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) ci.cancel();
    }
}
