package io.github.artynova.mediaworks.forge;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.projection.AstralPosition;
import io.github.artynova.mediaworks.projection.AstralDataSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MediaworksAbstractionsImpl {
    public static void setAstralPosition(ServerPlayerEntity player, @Nullable AstralPosition data) {
        AstralDataSerializer.putPlayerAstralPosition(player.getPersistentData(), data);
    }

    public static @Nullable AstralPosition getAstralPosition(ServerPlayerEntity player) {
        return AstralDataSerializer.getPlayerAstralPosition(player.getPersistentData());
    }


    public static void setAstralIota(ServerPlayerEntity player, @Nullable Iota iota) {
        AstralDataSerializer.putPlayerAstralIota(player.getPersistentData(), iota);
    }

    public static @Nullable Iota getAstralIota(ServerPlayerEntity player) {
        return AstralDataSerializer.getPlayerAstralIota(player.getPersistentData(), player.getWorld());
    }

    public static void setAstralOrigin(ServerPlayerEntity player, @Nullable Vec3d position) {
        AstralDataSerializer.putPlayerAstralOrigin(player.getPersistentData(), position);
    }

    public static @Nullable Vec3d getAstralOrigin(ServerPlayerEntity player) {
        return AstralDataSerializer.getPlayerAstralOrigin(player.getPersistentData());
    }
}
