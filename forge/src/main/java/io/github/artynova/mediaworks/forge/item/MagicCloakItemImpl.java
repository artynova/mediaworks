package io.github.artynova.mediaworks.forge.item;

import io.github.artynova.mediaworks.item.MagicCloakItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class MagicCloakItemImpl extends GeoArmorItem implements MagicCloakItem {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MagicCloakItemImpl() {
        super(MATERIAL, EquipmentSlot.HEAD, SETTINGS);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        MagicCloakItem.super.appendTooltip(stack, world, tooltip, context);
    }
}
