package io.github.artynova.mediaworks.api.logic;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

/**
 * Interface implemented by objects which are used as bases for platform-specific persistent data implementations.
 */
public interface PersistentDataContainer {
    void readFromNbt(@NotNull NbtCompound tag);

    void writeToNbt(@NotNull NbtCompound tag);
}
