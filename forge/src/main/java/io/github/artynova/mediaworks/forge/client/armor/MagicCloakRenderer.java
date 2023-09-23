package io.github.artynova.mediaworks.forge.client.armor;

import io.github.artynova.mediaworks.client.armor.ArmorLayersCulled;
import io.github.artynova.mediaworks.forge.item.MagicCloakItemImpl;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MagicCloakRenderer extends DyeableArmorRenderer<MagicCloakItemImpl> {
    public MagicCloakRenderer() {
        super(new MagicCloakModel());
        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
    }

    @Override
    public GeoArmorRenderer<MagicCloakItemImpl> applySlot(EquipmentSlot boneSlot) {
        super.applySlot(boneSlot);

        if (boneSlot == EquipmentSlot.HEAD) {
            setBoneVisibility(this.headBone, true);
            setBoneVisibility(this.bodyBone, true);
        }

        return this;
    }

    @Override
    public RenderLayer getRenderLayer(Identifier texture) {
        return ArmorLayersCulled.getArmorCutoutCull(texture);
    }

    @Override
    public VertexConsumer getArmorGlintConsumer(VertexConsumerProvider provider, RenderLayer layer, boolean solid, boolean glint) {
        return ArmorLayersCulled.getArmorGlintConsumer(provider, layer, solid, glint);
    }
}
