package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.logic.PersistentDataContainer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class Macula implements PersistentDataContainer {
    public static final int MAX_VISAGES = 32; // TODO maybe make this configurable?
    private final ServerPlayerEntity owner;
    private MaculaContent content = new MaculaContent();

    public Macula(ServerPlayerEntity owner) {
        this.owner = owner;
    }

    public MaculaContent getContent() {
        return content;
    }

    public void setContent(MaculaContent content) {
        this.content = content;
    }

    public void add(Visage visage) {
        if (visage.hasTimedOut(owner.getWorld().getTime())) return;
        content.add(visage);
    }

    public void trim() {
        content.removeIf(visage -> visage.hasTimedOut(owner.getWorld().getTime()));
    }

    public void clear() {
        content.clear();
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        content = MaculaSerializer.getContent(tag, owner.getWorld());
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        MaculaSerializer.putContent(tag, content, owner.getWorld());
    }
}
