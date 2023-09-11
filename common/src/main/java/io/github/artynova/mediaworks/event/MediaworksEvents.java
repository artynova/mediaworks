package io.github.artynova.mediaworks.event;

import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import io.github.artynova.mediaworks.macula.MaculaServer;
import io.github.artynova.mediaworks.projection.AstralProjectionServer;

public class MediaworksEvents {
    public static void init() {
        TickEvent.ServerLevelTick.PLAYER_POST.register(AstralProjectionServer::handlePlayerTick);
        PlayerEvent.PLAYER_JOIN.register(AstralProjectionServer::handleJoin);
        EntityEvent.LIVING_DEATH.register(AstralProjectionServer::handleDeath);
        PlayerEvent.CHANGE_DIMENSION.register(AstralProjectionServer::handleDimensionChange);

        PlayerEvent.PLAYER_CLONE.register(MaculaServer::handleClone);
        PlayerEvent.PLAYER_JOIN.register(MaculaServer::handleJoin);
    }
}
