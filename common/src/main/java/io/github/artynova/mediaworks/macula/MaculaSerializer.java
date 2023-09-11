package io.github.artynova.mediaworks.macula;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MaculaSerializer {
    public static final String MACULA_TAG = "mediaworks:macula";

    /**
     * @param compound an NbtComponent which has an {@link NbtList} entry for the macula.
     * @param world    the player's world, for checking world age.
     */
    public static Macula getMacula(@NotNull NbtCompound compound, World world) {
        return Macula.deserialize(compound.getList(MACULA_TAG, NbtElement.COMPOUND_TYPE), world.getTime());
    }

    /**
     * @param compound an NbtComponent into which the macula {@link NbtList} will be written.
     * @param macula   the macula.
     * @param world    the player's world, for checking world age.
     */
    public static void putMacula(@NotNull NbtCompound compound, @NotNull Macula macula, @NotNull World world) {
        compound.put(MACULA_TAG, Macula.serialize(macula, world.getTime()));
    }
}
