package io.github.artynova.mediaworks;

import io.github.artynova.mediaworks.client.event.MediaworksClientEvents;
import io.github.artynova.mediaworks.client.keybinding.MediaworksKeybindings;

public class MediaworksClient {
    public static void init() {
        MediaworksKeybindings.init();
        MediaworksClientEvents.init();
    }
}
