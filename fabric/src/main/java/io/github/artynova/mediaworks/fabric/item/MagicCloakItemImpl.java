package io.github.artynova.mediaworks.fabric.item;

import io.github.artynova.mediaworks.item.MagicCloakItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class MagicCloakItemImpl extends ArmorItem implements MagicCloakItem {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MagicCloakItemImpl() {
        super(MATERIAL, EquipmentSlot.HEAD, SETTINGS);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
