package io.github.artynova.mediaworks.logic.macula;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MaculaSerializer {
    public static final String CONTENT_TAG = "content";

    /**
     * @param compound an NbtComponent which has an {@link NbtList} entry for the macula content.
     * @param world    the player's world, for checking world age.
     */
    public static MaculaContent getContent(@NotNull NbtCompound compound, World world) {
        return MaculaContent.deserialize(compound.getList(CONTENT_TAG, NbtElement.COMPOUND_TYPE), world.getTime());
    }

    /**
     * @param compound an NbtComponent into which the macula content {@link NbtList} will be written.
     * @param maculaContent   the macula.
     * @param world    the player's world, for checking world age.
     */
    public static void putContent(@NotNull NbtCompound compound, @NotNull MaculaContent maculaContent, @NotNull World world) {
        compound.put(CONTENT_TAG, MaculaContent.serialize(maculaContent, world.getTime()));
    }
}
