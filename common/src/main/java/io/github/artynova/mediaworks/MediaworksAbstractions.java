package io.github.artynova.mediaworks;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.Visage;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import io.github.artynova.mediaworks.networking.SyncMaculaS2CMsg;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

// Static helper methods that depend on the mod loader in some way.
public class MediaworksAbstractions {
    @ExpectPlatform
    public static @NotNull AstralProjection getProjection(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Macula getMacula(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setMacula(@NotNull ServerPlayerEntity player, @NotNull Macula macula) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void addToMacula(@NotNull ServerPlayerEntity player, @NotNull Visage visage) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void clearMacula(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void trimMacula(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    /**
     * Makes a {@link SyncMaculaS2CMsg} in the most efficient way available to the platform-specific implementation.
     */
    @ExpectPlatform
    public static SyncMaculaS2CMsg makeSyncMaculaMsg(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }
}
