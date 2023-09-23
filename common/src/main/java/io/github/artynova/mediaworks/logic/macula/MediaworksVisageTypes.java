package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.api.logic.macula.VisageType;
import io.github.artynova.mediaworks.api.registry.MediaworksRegistries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static io.github.artynova.mediaworks.api.MediaworksAPI.id;

public class MediaworksVisageTypes {
    private static final Map<Identifier, VisageType<?>> TYPES = new HashMap<>();
    public static final VisageType<TextVisage> TEXT = register("text", TextVisage.TYPE);

    public static void init() {
        for (Map.Entry<Identifier, VisageType<?>> entry : TYPES.entrySet())
            MediaworksRegistries.VISAGE_TYPES.register(entry.getKey(), entry::getValue);
    }

    public static <T extends VisageType<?>> T register(String name, T visageType) {
        TYPES.put(id(name), visageType);
        return visageType;
    }
}
