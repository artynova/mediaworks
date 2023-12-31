package io.github.artynova.mediaworks.api.logic;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for platform-specific wrappers of data containers.
 * On Fabric it is implemented by cardinal components, Forge - capabilities.
 * The aim is to expose the platform-independent object that both platform-dependent wrappers store.
 */
public interface PersistentDataWrapper<T extends PersistentDataContainer> {
    /**
     * @return the {@link PersistentDataContainer} instance wrapped by this object.
     */
    @NotNull T unwrap();
}
