package io.github.artynova.mediaworks.interop;

import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.interop.patchouli.PatchouliInterop;

public class MediaworksInterop {
    public static void init() {
        PatchouliInterop.init();
        MediaworksAbstractions.initLoaderSpecificInterop();
    }
}
