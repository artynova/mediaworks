package io.github.artynova.mediaworks.networking;

import dev.architectury.networking.NetworkChannel;
import io.github.artynova.mediaworks.networking.projection.*;
import net.minecraft.server.network.ServerPlayerEntity;

import static io.github.artynova.mediaworks.Mediaworks.id;

// I love networking with Architectury, that's all I wanted to say
public class MediaworksNetworking {
    private static final NetworkChannel CHANNEL = NetworkChannel.create(id("networking_channel"));

    public static void init() {
        CHANNEL.register(SyncAstralPositionC2SMsg.class, SyncAstralPositionC2SMsg::encode, SyncAstralPositionC2SMsg::new, SyncAstralPositionC2SMsg::apply);
        CHANNEL.register(SyncAstralPositionS2CMsg.class, SyncAstralPositionS2CMsg::encode, SyncAstralPositionS2CMsg::new, SyncAstralPositionS2CMsg::apply);
        CHANNEL.register(EndProjectionC2SMsg.class, EndProjectionC2SMsg::encode, EndProjectionC2SMsg::new, EndProjectionC2SMsg::apply);
        CHANNEL.register(EndProjectionS2CMsg.class, EndProjectionS2CMsg::encode, EndProjectionS2CMsg::new, EndProjectionS2CMsg::apply);
        CHANNEL.register(SpawnHexParticlesS2CMsg.class, SpawnHexParticlesS2CMsg::encode, SpawnHexParticlesS2CMsg::new, SpawnHexParticlesS2CMsg::apply);
        CHANNEL.register(CastAstralIotaC2SMsg.class, CastAstralIotaC2SMsg::encode, CastAstralIotaC2SMsg::new, CastAstralIotaC2SMsg::apply);

        CHANNEL.register(SyncMaculaS2CMsg.class, SyncMaculaS2CMsg::encode, SyncMaculaS2CMsg::new, SyncMaculaS2CMsg::apply);
    }

    public static <T> void sendToServer(T message) {
        CHANNEL.sendToServer(message);
    }

    public static <T> void sendToPlayer(ServerPlayerEntity player, T message) {
        CHANNEL.sendToPlayer(player, message);
    }

    public static <T> void sendToPlayers(Iterable<ServerPlayerEntity> players, T message) {
        CHANNEL.sendToPlayers(players, message);
    }
}
