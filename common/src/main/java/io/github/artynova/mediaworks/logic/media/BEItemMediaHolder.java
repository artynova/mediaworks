package io.github.artynova.mediaworks.logic.media;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.function.IntSupplier;

/**
 * Container media holder implementation for {@link BlockEntity}-based container items, like shulker boxes.
 */
public class BEItemMediaHolder extends ContainerItemMediaHolder {
    public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
    public static final String ITEMS_TAG = "Items";
    public static final String SLOT_TAG = "Slot";

    private final IntSupplier inventorySizeSupplier;

    public BEItemMediaHolder(ItemStack stack, IntSupplier inventorySizeSupplier) {
        super(stack);
        this.inventorySizeSupplier = inventorySizeSupplier;
    }

    @Override
    protected List<ItemStack> getInventory() {
        int inventorySize = inventorySizeSupplier.getAsInt();
        List<ItemStack> inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);

        NbtCompound blockEntityCompound = getStack().getSubNbt(BLOCK_ENTITY_TAG);
        if (blockEntityCompound == null || !blockEntityCompound.contains(ITEMS_TAG, NbtElement.LIST_TYPE)) {
            return inventory;
        }

        NbtList itemList = blockEntityCompound.getList(ITEMS_TAG, NbtElement.COMPOUND_TYPE);
        if (itemList == null) return inventory;

        for (int i = 0, len = itemList.size(); i < len; ++i) {
            NbtCompound itemCompound = itemList.getCompound(i);
            if (!itemCompound.contains(SLOT_TAG, NbtElement.NUMBER_TYPE)) continue;

            int slot = itemCompound.getInt(SLOT_TAG);
            if (slot < 0 || slot >= inventorySize) continue;

            inventory.set(slot, ItemStack.fromNbt(itemCompound));
        }
        return inventory;
    }

    @Override
    protected void setInventory(List<ItemStack> inventory) {
        int inventorySize = inventorySizeSupplier.getAsInt();
        if (inventory.size() > inventorySize) inventory.subList(0, inventorySize);

        NbtList itemList = new NbtList();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (itemStack.isEmpty()) continue;

            NbtCompound itemCompound = new NbtCompound();
            itemStack.writeNbt(itemCompound);
            itemCompound.putInt(SLOT_TAG, i);
            itemList.add(itemCompound);
        }

        NbtCompound blockEntityCompound = new NbtCompound();
        blockEntityCompound.put(ITEMS_TAG, itemList);

        getStack().setSubNbt(BLOCK_ENTITY_TAG, blockEntityCompound);
    }
}
