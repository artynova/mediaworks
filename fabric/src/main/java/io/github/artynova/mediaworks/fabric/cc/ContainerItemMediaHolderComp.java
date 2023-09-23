package io.github.artynova.mediaworks.fabric.cc;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.fabric.cc.adimpl.CCMediaHolder;
import io.github.artynova.mediaworks.api.logic.media.ContainerItemMediaHolder;

/**
 * A component that delegates all its {@link ADMediaHolder} work to the {@link #commonDelegate} object.
 * Cannot use the common {@link ContainerItemMediaHolder} directly because the item component needs to
 * extend {@link CCMediaHolder}.
 */
public class ContainerItemMediaHolderComp extends CCMediaHolder {
    private final ContainerItemMediaHolder commonDelegate;

    public ContainerItemMediaHolderComp(ContainerItemMediaHolder commonDelegate) {
        super(commonDelegate.getStack());
        this.commonDelegate = commonDelegate;
    }

    @Override
    public int getMedia() {
        return commonDelegate.getMedia();
    }

    @Override
    public void setMedia(int media) {
        commonDelegate.setMedia(media);
    }

    @Override
    public int getMaxMedia() {
        return commonDelegate.getMaxMedia();
    }

    @Override
    public boolean canRecharge() {
        return commonDelegate.canRecharge();
    }

    @Override
    public boolean canProvide() {
        return commonDelegate.canProvide();
    }

    @Override
    public int getConsumptionPriority() {
        return commonDelegate.getConsumptionPriority();
    }

    @Override
    public boolean canConstructBattery() {
        return commonDelegate.canConstructBattery();
    }

    @Override
    public int withdrawMedia(int cost, boolean simulate) {
        return commonDelegate.withdrawMedia(cost, simulate);
    }
}
