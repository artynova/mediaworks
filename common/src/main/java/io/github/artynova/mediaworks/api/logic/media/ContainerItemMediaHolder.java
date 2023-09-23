package io.github.artynova.mediaworks.api.logic.media;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;
import io.github.artynova.mediaworks.util.HexUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * A cross-platform media holder that performs its functions based on items
 * stored within a container item, e.g. a shulker box, or perhaps some sort of modded backpack.
 */
public abstract class ContainerItemMediaHolder implements ADMediaHolder {
    public static final int PRIORITY = 500; // containers are only considered when all directly accessible media is exhausted
    protected ItemStack stack;

    public ContainerItemMediaHolder(ItemStack stack) {
        this.stack = stack;
    }

    /**
     * Core method of the {@link ContainerItemMediaHolder} class,
     * retrieves the item stacks stored in the base container.
     * <br/>
     * The returned list is later used to update the inventory after withdrawing.
     *
     * @return Item stacks for all inventory slots in order, including empty stacks ({@link ItemStack#EMPTY}).
     */
    protected abstract List<ItemStack> getInventory();

    /**
     * Core method of the {@link ContainerItemMediaHolder} class,
     * stores the item stack in the inventory.
     * <br/>
     * Number of elements is guaranteed to be the same as in the inventory returned by {@link #getInventory()}
     * in the same operation.
     *
     * @param inventory The new inventory item stacks inventory, in order of slots, with {@link ItemStack#EMPTY} for empty slots.
     */
    protected abstract void setInventory(List<ItemStack> inventory);

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public int getMedia() {
        return withdrawMedia(-1, true);
    }

    @Override
    public void setMedia(int media) {
        // non-op, media is changed by inserting and removing items via container's preferred method
    }

    @Override
    public int getMaxMedia() {
        return getMedia();
    }

    @Override
    public boolean canRecharge() {
        return false;
    }

    @Override
    public boolean canProvide() {
        return true;
    }

    @Override
    public int getConsumptionPriority() {
        return PRIORITY;
    }

    @Override
    public boolean canConstructBattery() {
        return false;
    }

    @Override
    public int withdrawMedia(int cost, boolean simulate) {
        int costLeft = cost;
        List<ItemStack> inventory = getInventory();
        List<ADMediaHolder> holders = HexUtils.collectMediaHolders(inventory);
        for (ADMediaHolder holder : holders) {
            costLeft -= MediaHelper.extractMedia(holder, costLeft, false, simulate);
            // the first condition is needed because with negative cost (= need to extract all media) costLeft would be negative even though we need to keep going
            if (cost >= 0 && costLeft <= 0) break;
        }
        if (!simulate)
            setInventory(inventory); // stacks themselves are updated by holders, so all we need to do is update the container's stack
        return cost - costLeft;
    }

    // insertMedia is fine: it becomes a non-op since getMedia() and getMaxMedia() return the same value
}
