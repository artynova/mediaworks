package io.github.artynova.mediaworks.forge.client.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.artynova.mediaworks.client.armor.DyeableArmorModel;
import io.github.artynova.mediaworks.interop.patchouli.PatchouliInterop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.compat.PatchouliCompat;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.Arrays;

public abstract class DyeableArmorRenderer<T extends ArmorItem & IAnimatable & DyeableItem> extends GeoArmorRenderer<T> implements IGeoRendererAccessWorkaround<T> {
    protected DyeableArmorModel<T> modelProvider;
    protected @Nullable Identifier currentTexture;

    public DyeableArmorRenderer(DyeableArmorModel<T> modelProvider) {
        super(modelProvider);
        this.modelProvider = modelProvider;
    }

    // most of the code is taken from GeoArmorRenderer,
    // modifications include getting the color from context via accessors
    // and rendering both with the colored texture and with the static overlay
    @Override
    public void render(float partialTick, MatrixStack matrixStack, VertexConsumer buffer, int packedLight) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(this.currentArmorItem));
        AnimationEvent<T> animationEvent = new AnimationEvent<>(this.currentArmorItem, 0, 0, MinecraftClient.getInstance().getPartialTick(), false, Arrays.asList(this.itemStack, this.entityLiving, this.armorSlot));

        matrixStack.push();
        matrixStack.translate(0, 24 / 16F, 0);
        matrixStack.scale(-1, -1, 1);

        this.dispatchedMat = matrixStack.peek().getPositionMatrix().copy();

        this.modelProvider.setLivingAnimations(this.currentArmorItem, getInstanceId(this.currentArmorItem), animationEvent); // TODO change to setCustomAnimations in 1.20+
        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        fitToBiped();

        currentTexture = this.modelProvider.getTextureResource(currentArmorItem);

        RenderSystem.setShaderTexture(0, currentTexture);
        Color renderColor = getRenderColor(this.currentArmorItem, partialTick, matrixStack, null, buffer, packedLight);
        RenderLayer dyeableRenderType = getRenderType(this.currentArmorItem, partialTick, matrixStack, null, buffer, packedLight, currentTexture);
        VertexConsumerProvider bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        buffer = ItemRenderer.getArmorGlintConsumer(bufferSource, RenderLayer.getArmorCutoutNoCull(currentTexture), false, itemStack.hasGlint());

        render(model, this.currentArmorItem, partialTick, dyeableRenderType, matrixStack, bufferSource, buffer, packedLight, OverlayTexture.DEFAULT_UV, renderColor.getRed() / 255f, renderColor.getGreen() / 255f, renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);


        currentTexture = this.modelProvider.getOverlayTextureResource(currentArmorItem);

        RenderSystem.setShaderTexture(0, currentTexture);
        RenderLayer overlayRenderType = getRenderType(this.currentArmorItem, partialTick, matrixStack, null, buffer, packedLight, currentTexture);
        buffer = ItemRenderer.getArmorGlintConsumer(bufferSource, RenderLayer.getArmorCutoutNoCull(currentTexture), false, itemStack.hasGlint());

        render(model, this.currentArmorItem, partialTick, overlayRenderType, matrixStack, bufferSource, buffer, packedLight, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        if (PatchouliInterop.isPresent()) {
            PatchouliCompat.patchouliLoaded(matrixStack);
        }

        currentTexture = null;
        matrixStack.pop();
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack poseStack, VertexConsumer buffer, int packedLight,
                                  int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingXform()) {
            Matrix4f poseState = poseStack.peek().getPositionMatrix();
            Vec3d renderOffset = getRenderOffset(this.currentArmorItem, 1);
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.dispatchedMat);

            bone.setModelSpaceXform(RenderUtils.invertAndMultiplyMatrices(poseState, this.renderEarlyMat));
            localMatrix.addToLastColumn(new Vec3f(renderOffset));
            bone.setLocalSpaceXform(localMatrix);
        }

        buffer = getArmorGlintConsumer(getCurrentRTB(),
                getRenderLayer(getTextureLocation(currentArmorItem)), false, itemStack.hasGlint());

        IGeoRendererAccessWorkaround.super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
                alpha);
    }

    /**
     * Can be customized to get fundamentally different glints, by default delegates the work to
     * {@link ItemRenderer#getArmorGlintConsumer(VertexConsumerProvider, RenderLayer, boolean, boolean)}.
     */
    public VertexConsumer getArmorGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return ItemRenderer.getArmorGlintConsumer(provider, layer, solid, glint);
    }

    public RenderLayer getRenderLayer(Identifier texture) {
        return RenderLayer.getArmorCutoutNoCull(texture);
    }

    @Override
    public Color getRenderColor(T instance, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn) {
        return Color.ofOpaque(instance.getColor(itemStack == null ? new ItemStack(instance) : itemStack));
    }

    @Override
    public Identifier getTextureLocation(T instance) {
        return currentTexture == null ? modelProvider.getTextureResource(instance) : currentTexture;
    }
}
