package io.github.artynova.mediaworks.casting;

import at.petrak.hexcasting.api.misc.DamageSourceOvercast;

/**
 * Mutable extension of the Overcast damage source designed to carry information
 * about the state of the Reciprocation self-damage loop that can happen when overcasting.
 */
public class DamageSourceReciprocationOvercast extends DamageSourceOvercast {
    private final int reps;

    public DamageSourceReciprocationOvercast(int reps) {
        this.reps = reps;
    }

    public int getReps() {
        return reps;
    }
}
