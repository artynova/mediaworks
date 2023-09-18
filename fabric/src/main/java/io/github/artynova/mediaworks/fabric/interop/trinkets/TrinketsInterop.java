package io.github.artynova.mediaworks.fabric.interop.trinkets;

import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.common.lib.HexItems;
import dev.architectury.platform.Platform;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.artynova.mediaworks.misc.LensTweaks;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class TrinketsInterop {
    public static final String MOD_ID = "trinkets";

    public static void init() {
        DiscoveryHandlers.addGridScaleModifier(TrinketsInterop::lensTrinketGridModifier);
    }

    public static float lensTrinketGridModifier(PlayerEntity player) {
        // bail if at least one other lens modifier is present
        if (LensTweaks.lensInEitherHand(player) || LensTweaks.lensOnHead(player)) return 1;
        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
        if (optional.isEmpty()) return 1;
        TrinketComponent component = optional.get();
        return component.isEquipped(HexItems.SCRYING_LENS) ? 0.75f : 1;
    }

    public static boolean isPresent() {
        return Platform.isModLoaded(MOD_ID);
    }
}
