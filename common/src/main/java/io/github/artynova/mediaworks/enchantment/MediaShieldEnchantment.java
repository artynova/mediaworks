package io.github.artynova.mediaworks.enchantment;

import at.petrak.hexcasting.api.mod.HexConfig;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import io.github.artynova.mediaworks.api.enchantment.CloakEnchantment;
import io.github.artynova.mediaworks.item.MediaworksItems;
import io.github.artynova.mediaworks.util.HexUtils;
import io.github.artynova.mediaworks.util.MathUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;

public class MediaShieldEnchantment extends CloakEnchantment {
    public static final int CUTOFF_LEVEL = 4;
    private static final Map<Integer, Float> RATIOS_CACHE = new HashMap<>();
    private static final float CUTOFF_LEVEL_ABSORPTION = getAbsorptionRatioForLevel(CUTOFF_LEVEL);
    // Absorption increase before diminishing returns start (pre-cutoff)
    private static final float BASE_ABSORPTION_PER_LEVEL = 0.1f;
    private static final float POST_CUTOFF_STEP_RATIO = 0.5f;
    
    public MediaShieldEnchantment() {
        super(Rarity.UNCOMMON);
    }

    public static float processIncomingDamage(PlayerEntity player, float amount) {
        if (amount <= 0) return amount;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return amount;
        ItemStack stack = serverPlayer.getEquippedStack(EquipmentSlot.HEAD);
        if (!stack.isOf(MediaworksItems.MAGIC_CLOAK.get())) return amount;

        int level = Math.min(CUTOFF_LEVEL, EnchantmentHelper.getLevel(MediaworksEnchantments.MEDIA_SHIELD.get(), stack));
        if (level <= 0) return amount;

        float maxAbsorbedAmount = amount * 0.1f * level; // 10% for 1 - 40% for 4; apparently fractional health is very much acceptable so no rounding
        int maxCost = (int) (maxAbsorbedAmount * HexConfig.common().mediaToHealthRate());

        int remainingCost = Math.max(0, consumeMedia(serverPlayer, stack, maxCost));

        // maxCost - remainingCost = actually consumed media, converting it back to health with the config ratio
        float absorbedAmount = (float) ((maxCost - remainingCost) / HexConfig.common().mediaToHealthRate());
        return Math.max(0, amount - absorbedAmount); // just in case
    }

    private static int consumeMedia(ServerPlayerEntity caster, ItemStack cloakStack, int mediaCost) {
        CastingContext context = new CastingContext(caster, Hand.OFF_HAND, CastingContext.CastSource.PACKAGED_HEX);
        HexUtils.extend(context).mediaworks$setForcedCastingStack(cloakStack);
        CastingHarness harness = new CastingHarness(context);

        int remaining = harness.withdrawMedia(mediaCost, context.getCanOvercast());

        HexUtils.extend(context).mediaworks$setForcedCastingStack(null);

        return remaining;
    }

    /**
     * Provides the absorption ratio (e.g. 0.5 for absorbing half the damage) from cache,
     * computing if missing.
     *
     * @param level level of the enchantment.
     * @return ratio of the damage absorbed. Is mathematically capped at 0.6 as the level grows indefinitely.
     */
    public static float getAbsorptionRatioForLevel(int level) {
        return RATIOS_CACHE.computeIfAbsent(level, MediaShieldEnchantment::computeAbsorptionRatioForLevel);
    }


    /**
     * Calculates the absorption ratio (e.g. 0.5 for absorbing half the damage).
     *
     * @param level level of the enchantment.
     * @return ratio of the damage absorbed. Is mathematically capped at 0.6 as the level grows indefinitely.
     */
    private static float computeAbsorptionRatioForLevel(int level) {
        if (level <= CUTOFF_LEVEL) return level * 0.1f;
        return CUTOFF_LEVEL_ABSORPTION + (float) MathUtils.geomProgressionSum(BASE_ABSORPTION_PER_LEVEL, POST_CUTOFF_STEP_RATIO, level - CUTOFF_LEVEL);
    }

    @Override
    public int getMaxLevel() {
        return CUTOFF_LEVEL;
    }
}
