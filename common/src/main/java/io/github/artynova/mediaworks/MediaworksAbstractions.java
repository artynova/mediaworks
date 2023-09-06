package io.github.artynova.mediaworks;

import at.petrak.hexcasting.api.spell.iota.Iota;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.artynova.mediaworks.projection.AstralPosition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

// Static helper methods that depend on the mod loader in some way.
public class MediaworksAbstractions {
    @ExpectPlatform
    public static void setAstralPosition(ServerPlayerEntity player, @Nullable AstralPosition position) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable AstralPosition getAstralPosition(ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setAstralIota(ServerPlayerEntity player, @Nullable Iota iota) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable Iota getAstralIota(ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void setAstralOrigin(ServerPlayerEntity player, @Nullable Vec3d position) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable Vec3d getAstralOrigin(ServerPlayerEntity player) {
        throw new AssertionError();
    }
}
