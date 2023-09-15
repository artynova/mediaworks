package io.github.artynova.mediaworks.interop.patchouli;

import at.petrak.hexcasting.interop.HexInterop;
import dev.architectury.platform.Platform;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.interop.moreiotas.MoreIotasInterop;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class PatchouliInterop {
    public static final List<String> INTEROP_ENTRY_MODS = List.of(MoreIotasInterop.MOD_ID);

    // modifies Hex Casting's any_interop flag to force the interop category if mediaworks interop entries are present
    public static void init() {
        boolean anyInterop = INTEROP_ENTRY_MODS.stream().anyMatch(Platform::isModLoaded);
        if (anyInterop) PatchouliAPI.get().setConfigFlag(HexInterop.PATCHOULI_ANY_INTEROP_FLAG, true);
    }
}
