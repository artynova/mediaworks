package io.github.artynova.mediaworks.forge.mixin;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.forge.cap.adimpl.CapItemMediaHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CapItemMediaHolder.class)
public class CapItemMediaHolderMixin {
    // fixes the tiny little bug due to which batteries in hex casting are consumed last instead of first
    @Inject(method = "getConsumptionPriority", at = @At("HEAD"), cancellable = true, remap = false)
    private void fixIncorrectlyHardcodedPriority(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ADMediaHolder.BATTERY_PRIORITY);
    }
}
