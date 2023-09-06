package io.github.artynova.mediaworks.mixin.projection;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Invoker
    void callRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
                          MatrixStack matrices, VertexConsumerProvider vertexConsumers);
}
