package io.github.artynova.mediaworks.forge.client.armor;

import io.github.artynova.mediaworks.forge.item.MagicCloakItemImpl;
import net.minecraft.entity.EquipmentSlot;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MagicCloakRenderer extends DyeableArmorRenderer<MagicCloakItemImpl> {
    public MagicCloakRenderer() {
        super(new MagicCloakModel());
        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        System.out.println("constructing the renderer from");
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            System.out.println(element);
        }
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
