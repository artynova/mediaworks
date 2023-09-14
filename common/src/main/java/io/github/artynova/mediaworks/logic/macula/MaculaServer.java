package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.networking.macula.SyncMaculaContentS2CMsg;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MaculaServer {
    public static final Map<ServerPlayerEntity, Macula> MACULA_CACHE = new HashMap<>();
    private static final Function<? super ServerPlayerEntity, ? extends Macula> CACHE_POPULATOR = MediaworksAbstractions::getMacula;

    public static Macula getMacula(ServerPlayerEntity player) {
        return MACULA_CACHE.computeIfAbsent(player, CACHE_POPULATOR);
    }

    public static void clearCache(ServerPlayerEntity player) {
        MACULA_CACHE.remove(player);
    }

    public static void handleClone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) {
        Macula oldMacula = getMacula(oldPlayer);
        Macula newMacula = getMacula(newPlayer);
        // copy screen dimensions in any case
        newMacula.setWidth(oldMacula.getWidth());
        newMacula.setHeight(oldMacula.getHeight());
        // update the new player data with the old macula content on returning from the end, no client sync is necessary
        if (wonGame) newMacula.setContent(oldMacula.getContent());
        // update the client with the empty macula on respawn
        else syncContentToClient(newPlayer);
        clearCache(oldPlayer);
    }

    /**
     * Syncs macula data to the joining client.
     */
    public static void handleJoin(ServerPlayerEntity player) {
        syncContentToClient(player);
    }

    /**
     * Clears the cached macula for the quitting player.
     */
    public static void handleQuit(ServerPlayerEntity player) {
        clearCache(player);
    }

    public static void syncContentToClient(ServerPlayerEntity player) {
        MediaworksNetworking.sendToPlayer(player, new SyncMaculaContentS2CMsg(getMacula(player)));
    }

    public static void syncDimensionsFromClient(ServerPlayerEntity player, int width, int height) {
        Macula macula = getMacula(player);
        macula.setWidth(width);
        macula.setHeight(height);
    }
}
