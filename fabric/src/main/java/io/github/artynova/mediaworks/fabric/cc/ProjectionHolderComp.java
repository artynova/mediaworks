package io.github.artynova.mediaworks.fabric.cc;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import io.github.artynova.mediaworks.logic.projection.AstralProjectionHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ProjectionHolderComp implements Component, AstralProjectionHolder {
    private final AstralProjection base;

    public ProjectionHolderComp(ServerPlayerEntity owner) {
        this.base = new AstralProjection(owner);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        base.readFromNbt(tag);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        base.writeToNbt(tag);
    }

    public @NotNull AstralProjection unwrap() {
        return base;
    }
}
