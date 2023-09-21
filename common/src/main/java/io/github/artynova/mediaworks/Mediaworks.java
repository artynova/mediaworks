package io.github.artynova.mediaworks;

import io.github.artynova.mediaworks.casting.iota.MediaworksIotaTypes;
import io.github.artynova.mediaworks.casting.pattern.MediaworksPatterns;
import io.github.artynova.mediaworks.effect.MediaworksEffects;
import io.github.artynova.mediaworks.enchantment.MediaworksEnchantments;
import io.github.artynova.mediaworks.event.MediaworksEvents;
import io.github.artynova.mediaworks.interop.MediaworksInterop;
import io.github.artynova.mediaworks.item.MediaworksItems;
import io.github.artynova.mediaworks.logic.macula.MediaworksVisageTypes;
import io.github.artynova.mediaworks.misc.MediaworksMisc;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.registry.MediaworksRegistries;
import io.github.artynova.mediaworks.sound.MediaworksSounds;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mediaworks {
    public static final String MOD_ID = "mediaworks";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        MediaworksRegistries.init();
        MediaworksMisc.init();
        MediaworksItems.init();
        MediaworksEnchantments.init();
        MediaworksEffects.init();
        MediaworksEvents.init();
        MediaworksNetworking.init();
        MediaworksSounds.init();
        MediaworksIotaTypes.init();
        MediaworksPatterns.init();
        MediaworksVisageTypes.init();
        MediaworksInterop.init();
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
