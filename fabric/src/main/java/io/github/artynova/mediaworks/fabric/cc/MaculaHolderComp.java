package io.github.artynova.mediaworks.fabric.cc;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import io.github.artynova.mediaworks.logic.PersistentDataWrapper;
import io.github.artynova.mediaworks.logic.macula.Macula;
import io.github.artynova.mediaworks.logic.macula.MaculaContent;
import io.github.artynova.mediaworks.logic.macula.MaculaSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class MaculaHolderComp implements Component, PersistentDataWrapper<Macula> {
    private final Macula base;

    public MaculaHolderComp(ServerPlayerEntity owner) {
        this.base = new Macula(owner);
    }

    @Override
    public @NotNull Macula unwrap() {
        return base;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        base.readFromNbt(tag);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        base.writeToNbt(tag);
    }
}
