package io.github.artynova.mediaworks.api.logic.macula;

import at.petrak.hexcasting.api.spell.iota.GarbageIota;
import io.github.artynova.mediaworks.api.client.macula.VisageRenderers;
import io.github.artynova.mediaworks.casting.iota.VisageIota;
import io.github.artynova.mediaworks.logic.macula.TextVisage;
import net.minecraft.text.MutableText;

/**
 * Data container for an instance of some VisageType, contains type-specific information required to reproduce the
 * given visage at any coordinates, fleeting or not.
 * Rendering functionality is registered clientside in {@link VisageRenderers}.
 */
public abstract class Visage {
    private final VisageType<?> type;

    /**
     * @param type Type object of the visage. Implementations are expected to ensure that its type parameter matches the actual visage class.
     */
    public Visage(VisageType<?> type) {
        this.type = type;
    }

    /**
     * @return A new instance of a "garbage" visage, i.e. an unbounded {@link TextVisage} displaying a {@link GarbageIota}.
     */
    public static Visage makeGarbageVisage() {
        return new TextVisage(TextVisage.captureText(new GarbageIota()));
    }

    public VisageType<?> getType() {
        return type;
    }

    /**
     * @return The colorless text display that represents a {@link VisageIota} holding this visage.
     */
    public abstract MutableText displayOnStack();
}
