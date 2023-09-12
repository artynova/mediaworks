package io.github.artynova.mediaworks.registry;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.logic.macula.VisageType;
import net.minecraft.util.Identifier;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksRegistries {
    public static final Registrar<VisageType<?>> VISAGE_TYPES = Registries.get(Mediaworks.MOD_ID).builder(id("visage_type"), new VisageType<?>[]{}).syncToClients().saveToDisc().build();
    public static void init() {
    }


    public static VisageType<?> getVisageType(Identifier id) {
        return VISAGE_TYPES.get(id);
    }
    public static Identifier getVisageTypeId(VisageType<?> type) {
        return VISAGE_TYPES.getId(type);
    }
}
