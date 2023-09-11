package io.github.artynova.mediaworks.fabric;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.fabric.cc.MediaworksCardinalComponents;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.MaculaSerializer;
import io.github.artynova.mediaworks.macula.Visage;
import io.github.artynova.mediaworks.networking.SyncMaculaS2CMsg;
import io.github.artynova.mediaworks.projection.AstralPosition;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MediaworksAbstractionsImpl {
    public static @Nullable AstralPosition getAstralPosition(ServerPlayerEntity player) {
        return MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).getPosition();
    }

    public static void setAstralPosition(ServerPlayerEntity player, @Nullable AstralPosition data) {
        MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).setPosition(data);
    }

    public static @Nullable Iota getAstralIota(ServerPlayerEntity player) {
        return MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).getIota();
    }

    public static void setAstralIota(ServerPlayerEntity player, @Nullable Iota iota) {
        MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).setIota(iota);
    }

    public static @Nullable Vec3d getAstralOrigin(ServerPlayerEntity player) {
        return MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).getOrigin();
    }

    public static void setAstralOrigin(ServerPlayerEntity player, @Nullable Vec3d position) {
        MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).setOrigin(position);
    }

    public static Macula getMacula(@NotNull ServerPlayerEntity player) {
        return MediaworksCardinalComponents.MACULA.get(player).getMacula();
    }

    public static void setMacula(@NotNull ServerPlayerEntity player, @NotNull Macula macula) {
        MediaworksCardinalComponents.MACULA.get(player).setMacula(macula);
    }

    public static void addToMacula(@NotNull ServerPlayerEntity player, @NotNull Visage visage) {
        Macula macula = MediaworksCardinalComponents.MACULA.get(player).getMacula();
        if (visage.hasTimedOut(player.getWorld().getTime())) return;
        macula.add(visage);
    }

    public static void clearMacula(@NotNull ServerPlayerEntity player) {
        MediaworksCardinalComponents.MACULA.get(player).setMacula(new Macula());
    }

    public static void trimMacula(@NotNull ServerPlayerEntity player) {
        Macula macula = MediaworksCardinalComponents.MACULA.get(player).getMacula();
        long currentTime = player.getWorld().getTime();
        macula.removeIf(visage -> visage.hasTimedOut(currentTime));
    }

    public static SyncMaculaS2CMsg makeSyncMaculaMsg(ServerPlayerEntity player) {
        Macula macula = MediaworksCardinalComponents.MACULA.get(player).getMacula();
        return new SyncMaculaS2CMsg(macula, player.getWorld());
    }
}
