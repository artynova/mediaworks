package net.hexdummy.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.hexdummy.DummyAbstractions;

import java.nio.file.Path;

public class DummyAbstractionsImpl {
    /**
     * This is the actual implementation of {@link DummyAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
