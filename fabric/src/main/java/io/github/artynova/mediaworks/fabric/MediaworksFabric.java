package io.github.artynova.mediaworks.fabric;

import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.interop.MediaworksInterop;
import net.fabricmc.api.ModInitializer;

public class MediaworksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Mediaworks.init();
    }
}
