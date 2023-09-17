package io.github.artynova.mediaworks;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.artynova.mediaworks.logic.macula.Macula;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

// Static helper methods that depend on the mod loader in some way.
public class MediaworksAbstractions {
    @ExpectPlatform
    public static @NotNull AstralProjection getProjection(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @NotNull Macula getMacula(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void initLoaderSpecificInterop() {
        throw new AssertionError();
    }
}
