package io.github.artynova.mediaworks.fabric;

import io.github.artynova.mediaworks.MediaworksClient;
import net.fabricmc.api.ClientModInitializer;

public class MediaworksClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MediaworksClient.init();
    }
}
