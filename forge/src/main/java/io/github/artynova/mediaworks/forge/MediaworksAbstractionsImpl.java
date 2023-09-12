package io.github.artynova.mediaworks.forge;

import io.github.artynova.mediaworks.forge.capabilities.MediaworksCapabilities;
import io.github.artynova.mediaworks.logic.macula.*;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MediaworksAbstractionsImpl {
    public static @NotNull AstralProjection getProjection(@NotNull ServerPlayerEntity player) {
        player.reviveCaps();
        Optional<AstralProjectionHolder> cap = player.getCapability(MediaworksCapabilities.PROJECTION_HOLDER_CAP).resolve();
        assert cap.isPresent(); // there should not be such a situation where this capability is missing
        return cap.get().unwrap();
    }

    public static @NotNull Macula getMacula(@NotNull ServerPlayerEntity player) {
        player.reviveCaps(); // sorry not sorry, invalidation only gets in the way
        Optional<MaculaHolder> cap = player.getCapability(MediaworksCapabilities.MACULA_HOLDER_CAP).resolve();
        assert cap.isPresent(); // there should not be such a situation where this capability is missing
        return cap.get().unwrap();
    }
}
