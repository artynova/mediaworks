package io.github.artynova.mediaworks.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.macula.VisageType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksRegistries {
    public static final RegistryKey<Registry<VisageType<?>>> VISAGE_TYPE_REGISTRY_KEY = RegistryKey.ofRegistry(id("visage_type"));
    private static final Registrar<VisageType<?>> VISAGE_TYPE_REGISTRAR = Registries.get(Mediaworks.MOD_ID).builder(id("visage_type"), new VisageType<?>[]{}).syncToClients().saveToDisc().build();
    public static void init() {
    }


    public static VisageType<?> getVisageType(Identifier id) {
        return VISAGE_TYPE_REGISTRAR.get(id);
    }
    public static Identifier getVisageTypeId(VisageType<?> type) {
        return Registries.getId(type, VISAGE_TYPE_REGISTRY_KEY);
    }
}
