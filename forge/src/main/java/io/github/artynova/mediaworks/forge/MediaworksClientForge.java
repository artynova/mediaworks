package io.github.artynova.mediaworks.forge;

import io.github.artynova.mediaworks.MediaworksClient;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MediaworksClientForge {
    public static void init(FMLClientSetupEvent event) {
        MediaworksClient.init();
    }
}
