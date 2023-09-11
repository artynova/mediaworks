package io.github.artynova.mediaworks;

import io.github.artynova.mediaworks.client.event.MediaworksClientEvents;
import io.github.artynova.mediaworks.client.macula.MediaworksVisageRenderers;

public class MediaworksClient {
    public static void init() {
        MediaworksClientEvents.init();
        MediaworksVisageRenderers.init();
    }
}
