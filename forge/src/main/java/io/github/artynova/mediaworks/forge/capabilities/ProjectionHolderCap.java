package io.github.artynova.mediaworks.forge.capabilities;

import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ProjectionHolderCap implements AstralProjectionHolder {
    private final AstralProjection base;

    public ProjectionHolderCap(ServerPlayerEntity owner) {
        this.base = new AstralProjection(owner);
    }

    @Override
    public @NotNull AstralProjection unwrap() {
        return base;
    }
}
