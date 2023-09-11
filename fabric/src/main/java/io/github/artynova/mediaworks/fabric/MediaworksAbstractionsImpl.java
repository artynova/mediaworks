package io.github.artynova.mediaworks.fabric;

import io.github.artynova.mediaworks.fabric.cc.MediaworksCardinalComponents;
import io.github.artynova.mediaworks.macula.Macula;
import io.github.artynova.mediaworks.macula.Visage;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import io.github.artynova.mediaworks.networking.SyncMaculaS2CMsg;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class MediaworksAbstractionsImpl {
    public static @NotNull AstralProjection getProjection(@NotNull ServerPlayerEntity player) {
        return MediaworksCardinalComponents.PROJECTION_HOLDER.get(player).unwrap();
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
