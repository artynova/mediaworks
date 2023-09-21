package io.github.artynova.mediaworks.mixin.cloak;

import io.github.artynova.mediaworks.item.MagicCloakItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// yes, this is shameless hackery
// but I'd much rather extend ArmorItem and force enchantments to think otherwise
// than create renderers for a new wearable from scratch
@Mixin(targets = {"net.minecraft.enchantment.EnchantmentTarget$1", "net.minecraft.enchantment.EnchantmentTarget$10"})
public class CloakIsNotArmorMixin {
    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void forceCloakNotArmor(Item item, CallbackInfoReturnable<Boolean> cir) {
        if (item instanceof MagicCloakItem) cir.setReturnValue(false);
    }
}
