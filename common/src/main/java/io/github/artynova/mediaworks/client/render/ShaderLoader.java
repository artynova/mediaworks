package io.github.artynova.mediaworks.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.io.IOException;

import static io.github.artynova.mediaworks.Mediaworks.LOGGER;
import static io.github.artynova.mediaworks.Mediaworks.id;

public class ShaderLoader implements SynchronousResourceReloader {
    private static final ShaderLoader INSTANCE = new ShaderLoader();
    private static final Identifier SHADER_ID = id("shaders/post/astral.json");
    private static ShaderEffect shader;

    private ShaderLoader() {
    }

    private static void loadShader() {
        try {
            if (shader != null) shader.close();
            shader = new ShaderEffect(MinecraftClient.getInstance().getTextureManager(), MinecraftClient.getInstance().getResourceManager(), MinecraftClient.getInstance().getFramebuffer(), SHADER_ID);
            setupShaderDimensions(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight());
        } catch (IOException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public static ShaderEffect getShader() {
        if (shader == null) loadShader(); // because forge does not fire the initial reload
        return shader;
    }

    public static void setupShaderDimensions(int width, int height) {
        if (shader != null) shader.setupDimensions(width, height);
    }

    public static ShaderLoader getInstance() {
        return INSTANCE;
    }

    @Override
    public void reload(ResourceManager manager) {
        loadShader();
    }
}
