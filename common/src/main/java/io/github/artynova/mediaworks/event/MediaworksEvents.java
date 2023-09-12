package io.github.artynova.mediaworks.event;

import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import io.github.artynova.mediaworks.client.macula.VisageRendererLoader;
import io.github.artynova.mediaworks.client.render.ShaderLoader;
import io.github.artynova.mediaworks.logic.macula.MaculaServer;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionServer;
import net.minecraft.resource.ResourceType;

public class MediaworksEvents {
    public static void init() {
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, ShaderLoader.getInstance());
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, VisageRendererLoader.getInstance());

        TickEvent.ServerLevelTick.PLAYER_POST.register(AstralProjectionServer::handlePlayerTick);
        PlayerEvent.PLAYER_JOIN.register(AstralProjectionServer::handleJoin);
        PlayerEvent.PLAYER_QUIT.register(AstralProjectionServer::handleQuit);
        PlayerEvent.PLAYER_CLONE.register(AstralProjectionServer::handleClone);
        EntityEvent.LIVING_DEATH.register(AstralProjectionServer::handleDeath);
        PlayerEvent.CHANGE_DIMENSION.register(AstralProjectionServer::handleDimensionChange);

        PlayerEvent.PLAYER_CLONE.register(MaculaServer::handleClone);
        PlayerEvent.PLAYER_JOIN.register(MaculaServer::handleJoin);
        PlayerEvent.PLAYER_JOIN.register(MaculaServer::handleQuit);
    }
}
