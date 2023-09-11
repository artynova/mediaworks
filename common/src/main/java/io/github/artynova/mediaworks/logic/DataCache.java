package io.github.artynova.mediaworks.logic;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Convenience extension to {@link HashMap} that allows to predefine a missing value
 * calculation function, use it to calculate values for unknown keys, and cache results
 * automatically.
 */
public class DataCache<K, V> extends HashMap<K, V> {
    private final Function<K, V> valueGetter;

    public DataCache(Function<K, V> valueGetter) {
        this.valueGetter = valueGetter;
    }

    public V getOrCompute(K key) {
        if (!containsKey(key)) put(key, valueGetter.apply(key));
        return get(key);
    }
}
