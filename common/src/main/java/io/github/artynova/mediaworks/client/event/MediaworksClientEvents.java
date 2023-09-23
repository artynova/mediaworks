package io.github.artynova.mediaworks.client.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import io.github.artynova.mediaworks.client.macula.MaculaClient;
import io.github.artynova.mediaworks.client.macula.VisageRendererLoader;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.client.render.ShaderLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import static io.github.artynova.mediaworks.Mediaworks.id;

@Environment(EnvType.CLIENT)
public class MediaworksClientEvents {
    public static final Identifier SHADER_RELOADER_ID = id("shader_reloader");
    public static final Identifier VISAGE_RENDERER_RELOADER_ID = id("visage_renderer_reloader");

    public static void init() {
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, VisageRendererLoader.getInstance(), VISAGE_RENDERER_RELOADER_ID);
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, ShaderLoader.getInstance(), SHADER_RELOADER_ID);

        ClientTickEvent.CLIENT_POST.register(AstralProjectionClient::handlePostTick);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(AstralProjectionClient::handleQuit);

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(MaculaClient::handleJoin);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(MaculaClient::handleQuit);
    }
}
