package io.github.artynova.mediaworks;

import io.github.artynova.mediaworks.casting.patterns.MediaworksPatterns;
import io.github.artynova.mediaworks.effect.MediaworksEffects;
import io.github.artynova.mediaworks.event.MediaworksEvents;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.sound.MediaworksSounds;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mediaworks {
    public static final String MOD_ID = "mediaworks";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void init() {
        MediaworksEffects.init();
        MediaworksPatterns.init();
        MediaworksEvents.init();
        MediaworksNetworking.init();
        MediaworksSounds.init();
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
