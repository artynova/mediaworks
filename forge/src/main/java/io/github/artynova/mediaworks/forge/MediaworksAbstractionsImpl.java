package io.github.artynova.mediaworks.forge;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.MaculaSerializer;
import io.github.artynova.mediaworks.macula.Visage;
import io.github.artynova.mediaworks.macula.VisageSerializer;
import io.github.artynova.mediaworks.networking.NbtCompoundMsg;
import io.github.artynova.mediaworks.networking.SyncMaculaS2CMsg;
import io.github.artynova.mediaworks.projection.AstralDataSerializer;
import io.github.artynova.mediaworks.projection.AstralPosition;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MediaworksAbstractionsImpl {
    public static @Nullable AstralPosition getAstralPosition(ServerPlayerEntity player) {
        return AstralDataSerializer.getPlayerAstralPosition(player.getPersistentData());
    }

    public static void setAstralPosition(ServerPlayerEntity player, @Nullable AstralPosition data) {
        AstralDataSerializer.putPlayerAstralPosition(player.getPersistentData(), data);
    }

    public static @Nullable Iota getAstralIota(ServerPlayerEntity player) {
        return AstralDataSerializer.getPlayerAstralIota(player.getPersistentData(), player.getWorld());
    }

    public static void setAstralIota(ServerPlayerEntity player, @Nullable Iota iota) {
        AstralDataSerializer.putPlayerAstralIota(player.getPersistentData(), iota);
    }

    public static @Nullable Vec3d getAstralOrigin(ServerPlayerEntity player) {
        return AstralDataSerializer.getPlayerAstralOrigin(player.getPersistentData());
    }

    public static void setAstralOrigin(ServerPlayerEntity player, @Nullable Vec3d position) {
        AstralDataSerializer.putPlayerAstralOrigin(player.getPersistentData(), position);
    }

    public static Macula getMacula(@NotNull ServerPlayerEntity player) {
        return MaculaSerializer.getMacula(player.getPersistentData(), player.getWorld());
    }

    public static void setMacula(@NotNull ServerPlayerEntity player, @NotNull Macula macula) {
        MaculaSerializer.putMacula(player.getPersistentData(), macula, player.getWorld());
    }

    public static void addToMacula(@NotNull ServerPlayerEntity player, @NotNull Visage visage) {
        if (visage.hasTimedOut(player.getWorld().getTime())) return;
        NbtCompound playerData = player.getPersistentData();
        if (!playerData.contains(MaculaSerializer.MACULA_TAG, NbtElement.LIST_TYPE)) {
            playerData.put(MaculaSerializer.MACULA_TAG, new NbtList());
        }
        NbtList list = playerData.getList(MaculaSerializer.MACULA_TAG, NbtElement.COMPOUND_TYPE);
        list.add(VisageSerializer.serialize(visage));
    }

    public static void clearMacula(@NotNull ServerPlayerEntity player) {
        MaculaSerializer.putMacula(player.getPersistentData(), new Macula(), player.getWorld());
    }

    public static void trimMacula(@NotNull ServerPlayerEntity player) {
        NbtList list = player.getPersistentData().getList(MaculaSerializer.MACULA_TAG, NbtElement.LIST_TYPE);
        if (list.isEmpty()) return;
        if (list.getHeldType() != NbtElement.COMPOUND_TYPE) return;
        long currentTime = player.getWorld().getTime();
        list.removeIf(element -> VisageSerializer.deserialize((NbtCompound) element, currentTime) == null);
    }

    public static SyncMaculaS2CMsg makeSyncMaculaMsg(ServerPlayerEntity player) {
        // directly feeding the NBT to the message constructor to avoid wasting time deserializing and re-serializing
        return new SyncMaculaS2CMsg(player.getPersistentData().getList(MaculaSerializer.MACULA_TAG, NbtElement.COMPOUND_TYPE));
    }
}
