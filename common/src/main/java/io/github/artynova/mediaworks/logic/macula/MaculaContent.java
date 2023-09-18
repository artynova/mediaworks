package io.github.artynova.mediaworks.logic.macula;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Macula-specialized Array List extension, for clearer code and as a container for
 * helper methods.
 */
public class MaculaContent extends ArrayList<VisageEntry> {
    public static final Comparator<VisageEntry> DEPTH_COMPARATOR = Comparator.comparingInt(entry -> entry.getOrigin().getZ());

    public static NbtList serialize(@NotNull MaculaContent maculaContent, long currentTime) {
        NbtList list = new NbtList();
        for (VisageEntry entry : maculaContent) {
            if (entry.hasTimedOut(currentTime)) continue;
            list.add(VisageSerializer.serializeEntry(entry));
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
            VisageEntry entry = VisageSerializer.deserializeEntry((NbtCompound) element);
            if (entry.hasTimedOut(currentTime)) continue;
            maculaContent.add(entry);
        }
        return maculaContent;
    }

    /**
     * Run a standard stable sort that arranges the entries in display order.
     */
    public void sortByDepth() {
        sort(DEPTH_COMPARATOR);
    }
}
