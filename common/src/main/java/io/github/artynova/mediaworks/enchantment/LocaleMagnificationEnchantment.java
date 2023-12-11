package io.github.artynova.mediaworks.enchantment;

import io.github.artynova.mediaworks.api.enchantment.CloakEnchantment;
import io.github.artynova.mediaworks.util.MathUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class LocaleMagnificationEnchantment extends CloakEnchantment {
    // standard max level, with scaling beyond it being drastically slowed ("cut off")
    public static final int CUTOFF_LEVEL = 3;

    // Will be checked quite often, so it's beneficial to cache specific formula outputs
    private static final Map<Integer, Double> INCREASE_CACHE = new HashMap<>();
    // Each next step is 0.5 of the last
    private static final double POST_CUTOFF_STEP_RATIO = 0.5;    private static final double CUTOFF_LEVEL_INCREASE = getIncreaseForLevel(CUTOFF_LEVEL);
    public LocaleMagnificationEnchantment() {
        super(Rarity.VERY_RARE);
    }    private static final double FIRST_POST_CUTOFF_INCREMENT = (CUTOFF_LEVEL_INCREASE - getIncreaseForLevel(CUTOFF_LEVEL - 1)) / 2.0;

    /**
     * Finds the ambit increase for the player based on the Locale Magnification enchantment on their cloak, if any.
     *
     * @param player the player
     * @return respective ambit increase (added to standard 32 blocks) according to the enchantment formula
     */
    public static double getAmbitIncrease(PlayerEntity player) {
        int level = EnchantmentHelper.getLevel(MediaworksEnchantments.LOCALE_MAGNIFICATION.get(), player.getEquippedStack(EquipmentSlot.HEAD));
        return getIncreaseForLevel(level);
    }

    /**
     * Retrieves the ambit increase for a given enchantment level (added to standard 32) from cache, computing if missing.
     *
     * @param level enchantment level
     * @return ambit increase. Is mathematically capped at 48 as the level grows indefinitely.
     */
    public static double getIncreaseForLevel(int level) {
        return INCREASE_CACHE.computeIfAbsent(level, LocaleMagnificationEnchantment::computeIncreaseForLevel);
    }

    /**
     * Calculates the ambit increase for a given enchantment level (added to standard 32).
     *
     * @param level enchantment level
     * @return ambit increase. Is mathematically capped at 48 as the level grows indefinitely.
     */
    private static double computeIncreaseForLevel(int level) {
        if (level == 0) return 0;
        if (level <= CUTOFF_LEVEL) return Math.pow(2, 2 + level);
        // after the cutoff, ambit increase steps are in a geometric progression
        return CUTOFF_LEVEL_INCREASE + MathUtils.geomProgressionSum(FIRST_POST_CUTOFF_INCREMENT, POST_CUTOFF_STEP_RATIO, level - CUTOFF_LEVEL);
    }

    @Override
    public int getMaxLevel() {
        return CUTOFF_LEVEL;
    }




}
