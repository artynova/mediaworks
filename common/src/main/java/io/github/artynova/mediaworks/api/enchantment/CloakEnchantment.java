package io.github.artynova.mediaworks.api.enchantment;

import io.github.artynova.mediaworks.MediaworksAbstractions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public abstract class CloakEnchantment extends Enchantment {
    public static final EnchantmentTarget CLOAK_TARGET = MediaworksAbstractions.getCloakEnchantmentTarget();

    public CloakEnchantment(Rarity weight) {
        super(weight, CLOAK_TARGET, new EquipmentSlot[]{EquipmentSlot.HEAD});
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }
}
