package io.github.artynova.mediaworks.misc;

import io.github.artynova.mediaworks.api.logic.media.BEContainerMediaHolder;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

import java.util.HashSet;
import java.util.Set;

public class ShulkerBoxMediaHolder extends BEContainerMediaHolder {
    public static final int INVENTORY_SIZE = 27;
    public static final Set<Item> SHULKER_BOX_ITEMS = new HashSet<>();

    static {
        SHULKER_BOX_ITEMS.add(ShulkerBoxBlock.get(null).asItem());
        for (DyeColor color : DyeColor.values()) SHULKER_BOX_ITEMS.add(ShulkerBoxBlock.get(color).asItem());
    }

    public ShulkerBoxMediaHolder(ItemStack stack) {
        super(stack, () -> INVENTORY_SIZE);
    }

    /**
     * Needed to properly attach the {@link BEContainerMediaHolder} to all shulker boxes.
     */
    public static boolean isShulkerBox(Item item) {
        return SHULKER_BOX_ITEMS.contains(item);
    }
}
