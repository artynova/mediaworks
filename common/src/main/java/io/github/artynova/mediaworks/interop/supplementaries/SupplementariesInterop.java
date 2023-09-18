package io.github.artynova.mediaworks.interop.supplementaries;

import dev.architectury.platform.Platform;

public class SupplementariesInterop {
    public static final String MOD_ID = "supplementaries";

    public static boolean isPresent() {
        return Platform.isModLoaded(MOD_ID);
    }
}
