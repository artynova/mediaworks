package io.github.artynova.mediaworks.api.client.macula;

import io.github.artynova.mediaworks.api.logic.macula.Visage;
import io.github.artynova.mediaworks.api.logic.macula.VisageType;
import io.github.artynova.mediaworks.api.registry.MediaworksRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;

// Taking notes from Entity Renderer organization in Minecraft
@Environment(EnvType.CLIENT)
public class VisageRenderers {

    private static final Map<VisageType<?>, VisageRendererFactory<?>> FACTORIES = new HashMap<>();

    public static <T extends Visage> void register(VisageType<T> type, VisageRendererFactory<T> factory) {
        FACTORIES.put(type, factory);
    }

    public static Map<VisageType<?>, VisageRenderer<?>> loadVisageRendererMap() {
        Map<VisageType<?>, VisageRenderer<?>> map = new HashMap<>();
        FACTORIES.forEach((type, factory) -> {
            try {
                map.put(type, factory.create());
            } catch (Exception e) {
                throw new RuntimeException("Failed to create renderer for " + MediaworksRegistries.getVisageTypeId(type));
            }
        });
        return map;
    }
}
