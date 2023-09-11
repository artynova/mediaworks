package io.github.artynova.mediaworks.macula;

import at.petrak.hexcasting.api.spell.iota.EntityIota;
import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.casting.iotas.Vec2Iota;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec2f;

public class MaculaServer {
    public static void handleClone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) {
        if (!wonGame) {
            // update the client with an empty macula if it was death
            syncToClient(newPlayer);
            return;
        }
        // update the new player with a filled macula if it was beating the end
        MediaworksAbstractions.setMacula(newPlayer, MediaworksAbstractions.getMacula(oldPlayer));
    }

    /**
     * Syncs macula data to the joining client.
     */
    public static void handleJoin(ServerPlayerEntity player) {
        syncToClient(player);
    }

    public static void syncToClient(ServerPlayerEntity player) {
        MediaworksNetworking.sendToPlayer(player, MediaworksAbstractions.makeSyncMaculaMsg(player));
    }
}
