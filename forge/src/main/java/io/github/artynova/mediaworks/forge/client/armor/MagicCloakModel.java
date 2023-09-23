package io.github.artynova.mediaworks.forge.client.armor;

import io.github.artynova.mediaworks.client.armor.DyeableArmorModel;
import io.github.artynova.mediaworks.forge.item.MagicCloakItemImpl;
import net.minecraft.util.Identifier;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MagicCloakModel extends DyeableArmorModel<MagicCloakItemImpl> {
    public static final Identifier MODEL_ID = id("geo/magic_cloak.geo.json");
    public static final Identifier TEXTURE_ID = id("textures/models/armor/magic_cloak.png");
    public static final Identifier OVERLAY_TEXTURE_ID = id("textures/models/armor/magic_cloak_overlay.png");
    public static final Identifier ANIMATION_ID = id("animations/magic_cloak.animation.json");

    @Override
    public Identifier getModelResource(MagicCloakItemImpl object) {
        return MODEL_ID;
    }

    @Override
    public Identifier getTextureResource(MagicCloakItemImpl object) {
        return TEXTURE_ID;
    }

    public Identifier getOverlayTextureResource(MagicCloakItemImpl object) {
        return OVERLAY_TEXTURE_ID;
    }

    @Override
    public Identifier getAnimationResource(MagicCloakItemImpl animatable) {
        return ANIMATION_ID;
    }
}
