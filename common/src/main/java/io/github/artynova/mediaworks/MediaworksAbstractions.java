package io.github.artynova.mediaworks;

import at.petrak.hexcasting.api.spell.iota.Iota;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.Visage;
import io.github.artynova.mediaworks.macula.VisageType;
import io.github.artynova.mediaworks.networking.SyncMaculaS2CMsg;
import io.github.artynova.mediaworks.projection.AstralPosition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Static helper methods that depend on the mod loader in some way.
public class MediaworksAbstractions {
    @ExpectPlatform
    public static @Nullable AstralPosition getAstralPosition(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setAstralPosition(@NotNull ServerPlayerEntity player, @Nullable AstralPosition position) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable Iota getAstralIota(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setAstralIota(@NotNull ServerPlayerEntity player, @Nullable Iota iota) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable Vec3d getAstralOrigin(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setAstralOrigin(@NotNull ServerPlayerEntity player, @Nullable Vec3d position) {
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
