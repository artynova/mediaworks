package io.github.artynova.mediaworks.fabric;

import io.github.artynova.mediaworks.Mediaworks;
import net.fabricmc.api.ModInitializer;

public class MediaworksFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Mediaworks.init();
    }
}
