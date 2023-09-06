package io.github.artynova.mediaworks.client.event;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import io.github.artynova.mediaworks.client.keybinding.MediaworksKeybindings;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.client.render.ShaderHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class MediaworksClientEvents {
    public static void init() {
        ClientTickEvent.CLIENT_POST.register(AstralProjectionClient::handlePostTick);
        ClientTickEvent.CLIENT_POST.register(MediaworksKeybindings::handlePostTick);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(AstralProjectionClient::handleQuit);
        ClientGuiEvent.RENDER_HUD.register(AstralProjectionClient::handleHudRender);
        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, new ShaderHandler());
    }
}
