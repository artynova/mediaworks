package io.github.artynova.mediaworks.misc;

import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.common.lib.HexItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

public class LensTweaks {
    public static void init() {
        DiscoveryHandlers.addGridScaleModifier(LensTweaks::lensHelmetGridModifier);
    }

    public static float lensHelmetGridModifier(PlayerEntity player) {
        // bail if at least one other standard lens modifier is present
        if (lensInEitherHand(player)) return 1;
        return lensOnHead(player) ? 0.75f : 1;
    }

    public static boolean lensOnHead(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.HEAD).isOf(HexItems.SCRYING_LENS);
    }

    public static boolean lensInEitherHand(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.MAINHAND).isOf(HexItems.SCRYING_LENS) || player.getEquippedStack(EquipmentSlot.OFFHAND).isOf(HexItems.SCRYING_LENS);
    }
}
