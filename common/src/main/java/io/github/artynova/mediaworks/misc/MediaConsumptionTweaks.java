package io.github.artynova.mediaworks.misc;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.utils.MediaHelper;

public class MediaConsumptionTweaks {
    /**
     * Compares instances of {@link ADMediaHolder} purely by their built-in priority, without regard for media amount.
     * Injecting this instead of standard logic in {@link MediaHelper#compareMediaItem(ADMediaHolder, ADMediaHolder)} in
     * makes it so that items within one priority are consumed in their original order (i.e.
     * inventory slot order), draining until empty.
     */
    public static int compareMediaItem(ADMediaHolder holder1, ADMediaHolder holder2) {
        return holder1.getConsumptionPriority() - holder2.getConsumptionPriority();
    }
}
