package io.github.artynova.mediaworks.logic.macula;

import net.minecraft.world.World;

/**
 * Data container for an instance of some VisageType.
 * Meant for data storage and client-server communication.
 * Rendering functionality is registered clientside in {@link io.github.artynova.mediaworks.client.macula.VisageRenderers}.
 */
public abstract class Visage {
    private final VisageType<?> type;
    private long endTime = -1;

    /**
     * @param type Type object of the visage. Implementations are expected to ensure that its type parameter matches the actual visage class.
     */
    public Visage(VisageType<?> type) {
        this.type = type;
    }

    public VisageType<?> getType() {
        return type;
    }

    /**
     * @return tick after which the visage is no longer displayed, or -1 if it should display until cleared.
     * @see World#getTime()
     */
    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        if (endTime < 0 && endTime != -1) {
            throw new IllegalArgumentException("Invalid endTime in Visage: " + endTime + ", must be -1 or >= 0");
        }
        this.endTime = endTime;
    }

    public boolean hasTimedOut(long currentTime) {
        return endTime > -1 && endTime <= currentTime;
    }
}
