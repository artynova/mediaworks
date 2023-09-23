package io.github.artynova.mediaworks.fabric.client.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.artynova.mediaworks.client.armor.DyeableArmorModel;
import io.github.artynova.mediaworks.fabric.mixin.GeoArmorRendererAccessor;
import io.github.artynova.mediaworks.interop.patchouli.PatchouliInterop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.compat.PatchouliCompat;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Arrays;

public class DyeableArmorRenderer<T extends ArmorItem & DyeableItem & IAnimatable> extends GeoArmorRenderer<T> {
    protected DyeableArmorModel<T> modelProvider;
    protected @Nullable Identifier currentTexture;
    public DyeableArmorRenderer(DyeableArmorModel<T> modelProvider) {
        super(modelProvider);
        this.modelProvider = modelProvider; // saving a proper reference with access to the added method
    }

    /**
     * Wraps a cast of the renderer to {@link GeoArmorRendererAccessor}.
     */
    public static <T extends ArmorItem & IAnimatable> GeoArmorRendererAccessor<T> extend(GeoArmorRenderer<T> renderer) {
        return (GeoArmorRendererAccessor<T>) renderer;
    }

    // most of the code is taken from GeoArmorRenderer,
    // modifications include getting the color from context via accessors
    // and rendering both with the colored texture and with the static overlay
    public void render(MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        GeoArmorRendererAccessor<T> accessor = extend(this);

        stack.translate(0.0D, 1.497F, 0.0D);
        stack.scale(-1.005F, -1.0F, 1.005F);
        this.setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        this.dispatchedMat = stack.peek().getPositionMatrix().copy();
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(currentArmorItem));

        AnimationEvent<T> itemEvent = new AnimationEvent<>(this.currentArmorItem, 0, 0,
                MinecraftClient.getInstance().getTickDelta(), false,
                Arrays.asList(this.itemStack, this.entityLiving, this.armorSlot));
        this.modelProvider.setLivingAnimations(currentArmorItem, this.getUniqueID(this.currentArmorItem), itemEvent);

        accessor.callFitToBiped();
        this.applySlot(armorSlot);
        stack.push();

        currentTexture = this.modelProvider.getTextureResource(currentArmorItem);
        RenderSystem.setShaderTexture(0, currentTexture);
        Color renderColor = getRenderColor(currentArmorItem, MinecraftClient.getInstance().getTickDelta(), stack, bufferIn,
                null, packedLightIn);
        RenderLayer renderLayer = getRenderType(currentArmorItem, MinecraftClient.getInstance().getTickDelta(), stack, bufferIn,
                null, packedLightIn, currentTexture);
        render(model, currentArmorItem, MinecraftClient.getInstance().getTickDelta(), renderLayer, stack, bufferIn, null,
                packedLightIn, OverlayTexture.DEFAULT_UV, (float) renderColor.getRed() / 255f,
                (float) renderColor.getGreen() / 255f, (float) renderColor.getBlue() / 255f,
                (float) renderColor.getAlpha() / 255);

        currentTexture = this.modelProvider.getOverlayTextureResource(currentArmorItem);
        RenderSystem.setShaderTexture(0, currentTexture);
        RenderLayer overlayRenderLayer = getRenderType(currentArmorItem, MinecraftClient.getInstance().getTickDelta(), stack, bufferIn,
                null, packedLightIn, currentTexture);
        render(model, currentArmorItem, MinecraftClient.getInstance().getTickDelta(), overlayRenderLayer, stack, bufferIn, null,
                packedLightIn, OverlayTexture.DEFAULT_UV, 1f,
                1f, 1f, 1f);

        if (PatchouliInterop.isPresent())
            PatchouliCompat.patchouliLoaded(stack);

        currentTexture = null;
        stack.pop();
        stack.scale(-1.005F, -1.0F, 1.005F);
        stack.translate(0.0D, -1.497F, 0.0D);
    }

    @Override
    public Color getRenderColor(T item, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn) {
        return Color.ofOpaque(item.getColor(itemStack == null ? new ItemStack(item) : itemStack));
    }

    @Override
    public Identifier getTextureLocation(T instance) {
        return currentTexture == null ? this.modelProvider.getTextureResource(instance) : currentTexture;
    }
}
