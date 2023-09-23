package io.github.artynova.mediaworks.client.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class DyeableArmorModel<T extends ArmorItem & IAnimatable & DyeableItem> extends AnimatedGeoModel<T> {
    public abstract Identifier getOverlayTextureResource(T object);
}
