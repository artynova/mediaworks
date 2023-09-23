package io.github.artynova.mediaworks.mixin.misc;

import at.petrak.hexcasting.interop.HexInterop;
import io.github.artynova.mediaworks.interop.patchouli.PatchouliInterop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HexInterop.class)
public class HexInteropMixin {
    // sorry not sorry, doing interop entries without a mixin is just not worth the amount of effort required to fix the loading order issues when on Forge
    @Inject(method = "initPatchouli", at = @At("TAIL"), remap = false)
    private static void initMediaworksPatchouli(CallbackInfo ci) {
        PatchouliInterop.init();
    }
}
