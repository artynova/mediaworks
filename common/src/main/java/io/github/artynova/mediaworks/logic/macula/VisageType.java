package io.github.artynova.mediaworks.logic.macula;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains information about a visage type.
 * {@link T} MUST match the type of the {@link Visage} implementation
 * that supplies the VisageType implementation instance as its type.
 */
public abstract class VisageType<T extends Visage> {
    /**
     * @return valid renderable visage, or null, in which case a garbage visage will be created instead.
     */
    public abstract @Nullable T deserializeData(NbtCompound compound);
    public abstract @NotNull NbtCompound serializeData(T visage);
}
