package io.github.artynova.mediaworks.fabric;

import io.github.artynova.mediaworks.fabric.cc.MediaworksCardinalComponents;
import io.github.artynova.mediaworks.fabric.interop.trinkets.TrinketsInterop;
import io.github.artynova.mediaworks.logic.macula.Macula;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class MediaworksAbstractionsImpl {
    public static @NotNull AstralProjection getProjection(@NotNull ServerPlayerEntity player) {
        return MediaworksCardinalComponents.PROJECTION_HOLDER.get(player).unwrap();
    }

    public static @NotNull Macula getMacula(@NotNull ServerPlayerEntity player) {
        return MediaworksCardinalComponents.MACULA_HOLDER.get(player).unwrap();
    }

    public static void initLoaderSpecificInterop() {
        if (TrinketsInterop.isPresent()) TrinketsInterop.init();
    }
}
