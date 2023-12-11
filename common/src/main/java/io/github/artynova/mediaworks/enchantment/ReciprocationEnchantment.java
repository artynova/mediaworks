package io.github.artynova.mediaworks.enchantment;

import at.petrak.hexcasting.api.addldata.ADHexHolder;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.github.artynova.mediaworks.api.enchantment.CloakEnchantment;
import io.github.artynova.mediaworks.casting.DamageSourceReciprocationOvercast;
import io.github.artynova.mediaworks.casting.ExtendedCastingContext;
import io.github.artynova.mediaworks.item.MediaworksItems;
import io.github.artynova.mediaworks.util.HexUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.util.List;

public class ReciprocationEnchantment extends CloakEnchantment {
    /**
     * Used to avoid stack overflow in case reciprocation loops due to casting from health,
     * but cannot kill the player for some reason.
     */
    public static final int MAX_RECIPROCATION_REPS = 50;

    public ReciprocationEnchantment() {
        super(Rarity.UNCOMMON);
    }

    public static void processPlayerHurt(DamageSource source, PlayerEntity maybeUser) {
        if (!(maybeUser instanceof ServerPlayerEntity player)) return;

        ItemStack stack = player.getEquippedStack(EquipmentSlot.HEAD);
        if (!stack.isOf(MediaworksItems.MAGIC_CLOAK.get())) return;

        int level = EnchantmentHelper.getLevel(MediaworksEnchantments.RECIPROCATION.get(), stack);
        if (level <= 0) return;

        ADHexHolder holder = IXplatAbstractions.INSTANCE.findHexHolder(stack);
        assert holder != null;
        List<Iota> hex = holder.getHex(player.getWorld());
        if (hex == null) return;

        CastingContext context = new CastingContext(player, Hand.OFF_HAND, CastingContext.CastSource.PACKAGED_HEX);
        ExtendedCastingContext extended = HexUtils.extend(context);
        extended.mediaworks$setForcedCastingStack(stack);
        // carry over previous reciprocation reps
        if (source instanceof DamageSourceReciprocationOvercast reciprocationOvercast) {
            int reps = reciprocationOvercast.getReps();
            if (reps >= MAX_RECIPROCATION_REPS) return; // terminate if we seem to be looping endlessly
            extended.mediaworks$setReciprocationReps(reps);
        }

        CastingHarness harness = new CastingHarness(context);
        harness.executeIotas(hex, player.getWorld());

        extended.mediaworks$setForcedCastingStack(null);
    }

    public int getMinPower(int level) {
        return 1;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 40;
    }
}
