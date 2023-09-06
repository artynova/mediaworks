package io.github.artynova.mediaworks.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Subsitute for fabric's WorldRenderContext, exposing only what is needed for this mod to work.
 */
@Environment(EnvType.CLIENT)
public class WorldRenderContext {
    private WorldRenderer worldRenderer;
    private MatrixStack matrixStack;
    private float tickDelta;
    private Camera camera;
    private VertexConsumerProvider consumers;

    public void setValues(WorldRenderer worldRenderer, MatrixStack matrixStack, float tickDelta, Camera camera, VertexConsumerProvider consumers) {
        this.worldRenderer = worldRenderer;
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
        this.camera = camera;
        this.consumers = consumers;
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public Camera getCamera() {
        return camera;
    }

    public VertexConsumerProvider getConsumers() {
        return consumers;
    }
}
