package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.logic.DataCache;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.networking.SyncMaculaS2CMsg;
import net.minecraft.server.network.ServerPlayerEntity;

public class MaculaServer {
    public static DataCache<ServerPlayerEntity, Macula> MACULA_CACHE = new DataCache<>(MediaworksAbstractions::getMacula);

    public static Macula getMacula(ServerPlayerEntity player) {
        return MACULA_CACHE.getOrCompute(player);
    }

    public static void clearCache(ServerPlayerEntity player) {
        MACULA_CACHE.remove(player);
    }

    public static void handleClone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) {
        if (!wonGame) {
            // update the client with an empty macula on respawn
            clearCache(oldPlayer);
            syncToClient(newPlayer);
            return;
        }
        // update the new player with a filled macula on returning from the end, no client sync is necessary
        getMacula(newPlayer).setContent(getMacula(oldPlayer).getContent());
        clearCache(oldPlayer);
    }

    /**
     * Syncs macula data to the joining client.
     */
    public static void handleJoin(ServerPlayerEntity player) {
        syncToClient(player);
    }

    /**
     * Clears the cached macula for the quitting player.
     */
    public static void handleQuit(ServerPlayerEntity player) {
        clearCache(player);
    }

    public static void syncToClient(ServerPlayerEntity player) {
        MediaworksNetworking.sendToPlayer(player, new SyncMaculaS2CMsg(getMacula(player)));
    }
}
