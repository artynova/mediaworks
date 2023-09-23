package io.github.artynova.mediaworks.fabric.client.armor;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

/**
 * Needed to access {@link IGeoRenderer#renderRecursively(GeoBone, MatrixStack, VertexConsumer, int, int, float, float, float, float)}.
 */
public interface IGeoRendererAccessWorkaround<T extends IAnimatable> extends IGeoRenderer<T> {
    @Override
    default void renderRecursively(GeoBone bone, MatrixStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        IGeoRenderer.super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
