package io.github.artynova.mediaworks.mixin.projection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
    private void wrapUse(CallbackInfo ci) {
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

    // for simplicity's sake, make the game think there's no riding inventory so that there is only one point where we have to insert the actual handling code
    @ModifyExpressionValue(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasRidingInventory()Z"))
    private boolean cancelRidingInventory(boolean previous) {
        return previous && !AstralProjectionClient.isDissociated();
    }

    // I'd rather not cancel the entire handleInputEvents call, but this is required to bypass a problematic Inventorio mixin
    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onInventoryOpened()V"), cancellable = true)
    private void hijackOpenInventory(CallbackInfo ci) {
        if (AstralProjectionClient.isDissociated()) {
            AstralProjectionClient.initiateEarlyEnd();
            ci.cancel();
        }
    }

//    @WrapOperation(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1))
//    private void wrapOpenInventory(MinecraftClient instance, Screen screen, Operation<Void> operation) {
//        if (AstralProjectionClient.isDissociated()) AstralProjectionClient.initiateEarlyEnd();
//        else operation.call(instance, screen);
//    }

    @ModifyExpressionValue(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z", ordinal = 2))
    private boolean cancelDrop(boolean previous) {
        return previous || AstralProjectionClient.isDissociated();
    }
}
