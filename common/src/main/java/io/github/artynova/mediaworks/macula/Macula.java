package io.github.artynova.mediaworks.macula;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Macula-specialized Array List extension, for clearer code and as a container for
 * helper methods.
 */
public class Macula extends ArrayList<Visage> {
    public static NbtList serialize(@NotNull Macula macula, long currentTime) {
        NbtList list = new NbtList();
        for (Visage visage : macula) {
            if (visage.hasTimedOut(currentTime)) continue;
            list.add(VisageSerializer.serialize(visage));
        }
        return list;
    }

    /**
     * Deserializes a trimmed version of the macula (
     */
    public static Macula deserialize(@NotNull NbtList list, long currentTime) {
        Macula macula = new Macula();

        if (list.isEmpty()) return macula;
        if (list.getHeldType() != NbtElement.COMPOUND_TYPE) return macula;
        for (NbtElement element : list) {
            Visage visage = VisageSerializer.deserialize((NbtCompound) element, currentTime);
            if (visage == null) continue;
            macula.add(visage);
        }
        return macula;
    }

    /**
     * Removes all {@link Visage} nbt entries that cannot be drawn for any reason,
     * like being invalid or having timed out.
     */
    public static void trimMaculaData(@NotNull NbtList list, long currentTime) {
        if (list.isEmpty()) return;
        if (list.getHeldType() != NbtElement.COMPOUND_TYPE) return;
        list.removeIf(element -> VisageSerializer.deserialize((NbtCompound) element, currentTime) == null);
    }
}
