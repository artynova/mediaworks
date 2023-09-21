package io.github.artynova.mediaworks.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

public class LocaleMagnificationEnchantment extends CloakEnchantment {
    public static final int MAX_LEVEL = 3;
    public static final int[] BONUSES = new int[]{8, 16, 32};

    public LocaleMagnificationEnchantment() {
        super(Rarity.VERY_RARE);
    }

    public static int getAmbitIncrease(PlayerEntity player) {
        int level = Math.min(MAX_LEVEL, EnchantmentHelper.getLevel(MediaworksEnchantments.LOCALE_MAGNIFICATION.get(), player.getEquippedStack(EquipmentSlot.HEAD)));
        if (level <= 0) return 0;
        return BONUSES[level - 1];
    }

    @Override
    public int getMaxLevel() {
        return MAX_LEVEL;
    }
}
