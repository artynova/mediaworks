package io.github.artynova.mediaworks.client.keybinding;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import io.github.artynova.mediaworks.networking.EndProjectionC2SMsg;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class MediaworksKeybindings {
    public static final KeyBinding RETURN_TO_BODY = new KeyBinding("key.mediaworks.return", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_SEMICOLON, "category.mediaworks.main");

    public static void init() {
        KeyMappingRegistry.register(RETURN_TO_BODY);
    }

    public static void handlePostTick(MinecraftClient client) {
        if (AstralProjectionClient.isDissociated() && RETURN_TO_BODY.wasPressed()) {
            MediaworksNetworking.sendToServer(new EndProjectionC2SMsg());
        }
        // consume excess
        while (RETURN_TO_BODY.wasPressed()) ;
    }
}
