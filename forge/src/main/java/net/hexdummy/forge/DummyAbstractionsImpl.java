package net.hexdummy.forge;

import net.hexdummy.DummyAbstractions;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class DummyAbstractionsImpl {
    /**
     * This is the actual implementation of {@link DummyAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
