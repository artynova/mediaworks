package io.github.artynova.mediaworks.logic.projection;

import io.github.artynova.mediaworks.logic.PersistentDataWrapper;

/**
 * Common interface for platform-specific astral projection persistent storages.
 * Since the interface is not interacted with in the common code directly, it is more of a method specification.
 */
public interface AstralProjectionHolder extends PersistentDataWrapper<AstralProjection> {
}
