package io.github.artynova.mediaworks.casting.iota;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksIotaTypes {
    public static Map<Identifier, IotaType<?>> TYPES = new HashMap<>();
    public static IotaType<VisageIota> VISAGE = register("visage", VisageIota.TYPE);

    public static void init() {
        for (Map.Entry<Identifier, IotaType<?>> entry : TYPES.entrySet()) {
            Registry.register(HexIotaTypes.REGISTRY, entry.getKey(), entry.getValue());
        }
    }

    private static <U extends Iota, T extends IotaType<U>> T register(String name, T type) {
        IotaType<?> old = TYPES.put(id(name), type);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return type;
    }
}
