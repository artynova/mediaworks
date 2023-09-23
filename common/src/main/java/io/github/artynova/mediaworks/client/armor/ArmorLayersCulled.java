package io.github.artynova.mediaworks.client.armor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

/**
 * All of this is very much based on {@link RenderLayer} in vanilla Minecraft, except without using the
 * {@link RenderLayer#DISABLE_CULLING} option.
 * <p>
 * This is necessary for cloak rendering, to have double-sided textures without z-fighting and fix feather glint
 * problems occurring otherwise.
 */
@Environment(EnvType.CLIENT)
public class ArmorLayersCulled {
    private static final Function<Identifier, RenderLayer> ARMOR_CUTOUT_CULL = Util.memoize((texture) -> {
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().shader(RenderLayer.ARMOR_CUTOUT_NO_CULL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(RenderLayer.NO_TRANSPARENCY).lightmap(RenderLayer.ENABLE_LIGHTMAP).overlay(RenderLayer.ENABLE_OVERLAY_COLOR).layering(RenderLayer.VIEW_OFFSET_Z_LAYERING).build(true);
        return RenderLayer.of("armor_cutout_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });

    private static final RenderLayer ARMOR_GLINT_CULL = RenderLayer.of("armor_glint_cull", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder().shader(RenderLayer.ARMOR_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(RenderLayer.COLOR_MASK).depthTest(RenderLayer.EQUAL_DEPTH_TEST).transparency(RenderLayer.GLINT_TRANSPARENCY).texturing(RenderLayer.GLINT_TEXTURING).layering(RenderLayer.VIEW_OFFSET_Z_LAYERING).build(false));
    private static final RenderLayer ARMOR_ENTITY_GLINT_CULL = RenderLayer.of("armor_entity_glint_cull", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder().shader(RenderLayer.ARMOR_ENTITY_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(RenderLayer.COLOR_MASK)/*cull here*/.depthTest(RenderLayer.EQUAL_DEPTH_TEST).transparency(RenderLayer.GLINT_TRANSPARENCY).texturing(RenderLayer.ENTITY_GLINT_TEXTURING).layering(RenderLayer.VIEW_OFFSET_Z_LAYERING).build(false));

    public static RenderLayer getArmorCutoutCull(Identifier texture) {
        return ARMOR_CUTOUT_CULL.apply(texture);
    }

    public static RenderLayer getArmorGlintCull() {
        return ARMOR_GLINT_CULL;
    }

    public static RenderLayer getArmorEntityGlintCull() {
        return ARMOR_ENTITY_GLINT_CULL;
    }

    public static VertexConsumer getArmorGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return glint ? VertexConsumers.union(provider.getBuffer(solid ? getArmorGlintCull() : getArmorEntityGlintCull()), provider.getBuffer(layer)) : provider.getBuffer(layer);
    }
}
