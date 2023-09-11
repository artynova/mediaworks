package io.github.artynova.mediaworks.logic;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for platform-specific wrappers of data containers.
 * On Fabric it is implemented by cardinal components, Forge - capabilities.
 * The aim is to expose the underlying common implementation.
 */
public interface PersistentDataWrapper<T extends PersistentDataContainer> {
    /**
     * @return the {@link PersistentDataContainer} instance wrapped by this object.
     */
    @NotNull T unwrap();
}
