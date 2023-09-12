package io.github.artynova.mediaworks.logic.macula;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Macula-specialized Array List extension, for clearer code and as a container for
 * helper methods.
 */
public class MaculaContent extends ArrayList<Visage> {
    public static NbtList serialize(@NotNull MaculaContent maculaContent, long currentTime) {
        NbtList list = new NbtList();
        for (Visage visage : maculaContent) {
            if (visage.hasTimedOut(currentTime)) continue;
            list.add(VisageSerializer.serialize(visage));
        }
        return list;
    }

    /**
     * Deserializes a trimmed version of the macula (
     */
    public static MaculaContent deserialize(@NotNull NbtList list, long currentTime) {
        MaculaContent maculaContent = new MaculaContent();

        if (list.isEmpty()) return maculaContent;
        if (list.getHeldType() != NbtElement.COMPOUND_TYPE) return maculaContent;
        for (NbtElement element : list) {
            Visage visage = VisageSerializer.deserialize((NbtCompound) element, currentTime);
            if (visage == null) continue;
            maculaContent.add(visage);
        }
        return maculaContent;
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
