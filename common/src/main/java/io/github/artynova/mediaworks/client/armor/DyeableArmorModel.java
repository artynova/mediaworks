package io.github.artynova.mediaworks.client.armor;

import io.github.artynova.mediaworks.item.MagicCloakItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static io.github.artynova.mediaworks.Mediaworks.id;

public abstract class DyeableArmorModel<T extends ArmorItem & IAnimatable & DyeableItem> extends AnimatedGeoModel<T> {
    public abstract Identifier getOverlayTextureResource(T object);
}
