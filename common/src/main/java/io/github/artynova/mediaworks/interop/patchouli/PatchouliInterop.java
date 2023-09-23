package io.github.artynova.mediaworks.interop.patchouli;

import at.petrak.hexcasting.interop.HexInterop;
import dev.architectury.platform.Platform;
import io.github.artynova.mediaworks.interop.moreiotas.MoreIotasInterop;
import io.github.artynova.mediaworks.interop.supplementaries.SupplementariesInterop;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class PatchouliInterop {
    public static String MOD_ID = "patchouli";
    public static final List<String> INTEROP_ENTRY_MODS = List.of(MoreIotasInterop.MOD_ID, SupplementariesInterop.MOD_ID);
    public static final List<String> CONTAINER_INTEROP_MODS = List.of(SupplementariesInterop.MOD_ID);
    // whether any modded container items supported by the "container" media source are present
    public static final String ANY_CONTAINER_INTEROP_FLAG = "mediaworks:any_container_interop";

    // modifies Hex Casting's any_interop flag to force the interop category if mediaworks interop entries are present
    public static void init() {
        boolean anyInterop = INTEROP_ENTRY_MODS.stream().anyMatch(Platform::isModLoaded);
        if (anyInterop) PatchouliAPI.get().setConfigFlag(HexInterop.PATCHOULI_ANY_INTEROP_FLAG, true);

        boolean anyContainerInterop = CONTAINER_INTEROP_MODS.stream().anyMatch(Platform::isModLoaded);
        if (anyContainerInterop) PatchouliAPI.get().setConfigFlag(ANY_CONTAINER_INTEROP_FLAG, true);
    }

    public static boolean isPresent() {
        return Platform.isModLoaded(MOD_ID);
    }
}
