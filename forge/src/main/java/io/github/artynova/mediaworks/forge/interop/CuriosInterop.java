package io.github.artynova.mediaworks.forge.interop;

import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.common.lib.HexItems;
import dev.architectury.platform.Platform;
import io.github.artynova.mediaworks.misc.LensTweaks;
import net.minecraft.entity.player.PlayerEntity;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class CuriosInterop {
    public static final String MOD_ID = "curios";

    public static void init() {
        DiscoveryHandlers.addGridScaleModifier(CuriosInterop::lensCurioGridModifier);
    }

    // lens check code from hex casting itself: https://github.com/gamma-delta/HexMod/blob/c8510ed83d50ac7e05d91ba3f1924e21ec10d837/Forge/src/main/java/at/petrak/hexcasting/forge/interop/curios/CuriosApiInterop.java
    public static float lensCurioGridModifier(PlayerEntity player) {
        // bail if at least one other lens modifier is present
        if (LensTweaks.lensInEitherHand(player) || LensTweaks.lensOnHead(player)) return 1;
        AtomicBoolean hasLens = new AtomicBoolean(false);
        player.getCapability(CuriosCapability.INVENTORY).ifPresent(handler -> {
            ICurioStacksHandler stacksHandler = handler.getCurios().get("head");
            if (stacksHandler != null) {
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (stacks.getStackInSlot(i).isOf(HexItems.SCRYING_LENS)) {
                        hasLens.set(true);
                        break;
                    }
                }
            }
        });
        return hasLens.get() ? 0.75f : 1;
    }

    public static boolean isPresent() {
        return Platform.isModLoaded(MOD_ID);
    }
}
