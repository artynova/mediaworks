package io.github.artynova.mediaworks;

import io.github.artynova.mediaworks.client.event.MediaworksClientEvents;
import io.github.artynova.mediaworks.client.macula.MediaworksVisageRenderers;
import io.github.artynova.mediaworks.item.MagicCloakItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MediaworksClient {
    public static void init() {
        MediaworksClientEvents.init();
        MediaworksVisageRenderers.init();
    }
}
