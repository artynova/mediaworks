package io.github.artynova.mediaworks.forge;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.projection.AstralPosition;
import io.github.artynova.mediaworks.projection.ProjectionDataSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MediaworksAbstractionsImpl {
    public static void setAstralPosition(ServerPlayerEntity player, @Nullable AstralPosition data) {
        ProjectionDataSerializer.putPlayerAstralPosition(player.getPersistentData(), data);
    }

    public static @Nullable AstralPosition getAstralPosition(ServerPlayerEntity player) {
        return ProjectionDataSerializer.getPlayerAstralPosition(player.getPersistentData());
    }


    public static void setAstralIota(ServerPlayerEntity player, @Nullable Iota iota) {
        ProjectionDataSerializer.putPlayerAstralIota(player.getPersistentData(), iota);
    }

    public static @Nullable Iota getAstralIota(ServerPlayerEntity player) {
        return ProjectionDataSerializer.getPlayerAstralIota(player.getPersistentData(), player.getWorld());
    }

    public static void setAstralOrigin(ServerPlayerEntity player, @Nullable Vec3d position) {
        ProjectionDataSerializer.putPlayerAstralOrigin(player.getPersistentData(), position);
    }

    public static @Nullable Vec3d getAstralOrigin(ServerPlayerEntity player) {
        return ProjectionDataSerializer.getPlayerAstralOrigin(player.getPersistentData());
    }
}
