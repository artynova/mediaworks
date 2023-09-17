package io.github.artynova.mediaworks.mixin.misc;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;
import io.github.artynova.mediaworks.misc.MediaConsumptionTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MediaHelper.class)
public class MediaHelperMixin {
    @Inject(method = "compareMediaItem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void modifyComparisonLogic(ADMediaHolder aMedia, ADMediaHolder bMedia, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(MediaConsumptionTweaks.compareMediaItem(aMedia, bMedia));
    }
}
