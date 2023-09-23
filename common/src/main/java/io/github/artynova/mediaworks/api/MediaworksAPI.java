package io.github.artynova.mediaworks.api;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MediaworksAPI {
    public static final String MOD_ID = "mediaworks";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
