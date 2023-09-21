package io.github.artynova.mediaworks.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public abstract class CloakEnchantment extends Enchantment {
    public CloakEnchantment(Rarity weight) {
        super(weight, MediaworksEnchantments.CLOAK_TARGET, new EquipmentSlot[]{EquipmentSlot.HEAD});
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
