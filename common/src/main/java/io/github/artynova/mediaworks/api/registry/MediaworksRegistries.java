package io.github.artynova.mediaworks.api.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import io.github.artynova.mediaworks.api.MediaworksAPI;
import io.github.artynova.mediaworks.api.logic.macula.VisageType;
import net.minecraft.util.Identifier;

import static io.github.artynova.mediaworks.api.MediaworksAPI.id;

public class MediaworksRegistries {
    public static final Registrar<VisageType<?>> VISAGE_TYPES = Registries.get(MediaworksAPI.MOD_ID).builder(id("visage_type"), new VisageType<?>[]{}).syncToClients().saveToDisc().build();

    // empty, but called to load the class and run static initialization code
    public static void init() {
    }


    public static VisageType<?> getVisageType(Identifier id) {
        return VISAGE_TYPES.get(id);
    }

    public static Identifier getVisageTypeId(VisageType<?> type) {
        return VISAGE_TYPES.getId(type);
    }
}
