package io.github.artynova.mediaworks.interop.supplementaries;

import io.github.artynova.mediaworks.logic.media.BEContainerMediaHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.IntSupplier;

/**
 * Funnily enough, this does not even need any of the Supplementaries code to depend on.
 */
public class SackMediaHolder extends BEContainerMediaHolder {
    public static final Identifier SACK_ID = new Identifier(SupplementariesInterop.MOD_ID, "sack");
    public static final int INVENTORY_SIZE = 9;

    public SackMediaHolder(ItemStack stack) {
        super(stack, () -> INVENTORY_SIZE);
    }

    public static boolean isSack(Item item) {
        return SACK_ID.equals(Registry.ITEM.getId(item));
    }
}
