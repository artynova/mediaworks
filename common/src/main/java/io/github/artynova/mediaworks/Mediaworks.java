package io.github.artynova.mediaworks;

import io.github.artynova.mediaworks.api.registry.MediaworksRegistries;
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
import io.github.artynova.mediaworks.sound.MediaworksSounds;

public class Mediaworks {
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
}
