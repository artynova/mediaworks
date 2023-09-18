package io.github.artynova.mediaworks.client.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import io.github.artynova.mediaworks.client.macula.MaculaClient;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MediaworksClientEvents {
    public static void init() {
        ClientTickEvent.CLIENT_POST.register(AstralProjectionClient::handlePostTick);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(AstralProjectionClient::handleQuit);

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(MaculaClient::handleJoin);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(MaculaClient::handleQuit);
    }
}
