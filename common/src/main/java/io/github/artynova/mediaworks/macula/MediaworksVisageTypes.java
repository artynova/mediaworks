package io.github.artynova.mediaworks.macula;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.client.macula.TextVisageRenderer;
import io.github.artynova.mediaworks.registry.MediaworksRegistries;

public class MediaworksVisageTypes {
    public static final DeferredRegister<VisageType<?>> VISAGE_TYPES = DeferredRegister.create(Mediaworks.MOD_ID, MediaworksRegistries.VISAGE_TYPE_REGISTRY_KEY);
    public static final RegistrySupplier<VisageType<TextVisage>> TEXT = VISAGE_TYPES.register("text", () -> TextVisage.TYPE);

    public static void init() {
        VISAGE_TYPES.register();
    }

}
