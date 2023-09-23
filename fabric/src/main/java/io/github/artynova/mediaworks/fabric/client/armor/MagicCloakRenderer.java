package io.github.artynova.mediaworks.fabric.client.armor;

import io.github.artynova.mediaworks.fabric.item.MagicCloakItemImpl;
import net.minecraft.entity.EquipmentSlot;
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
}
