package io.github.artynova.mediaworks.logic.macula;

import io.github.artynova.mediaworks.api.logic.PersistentDataContainer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class Macula implements PersistentDataContainer {
    public static final int MAX_VISAGES = 32; // TODO maybe make this configurable?
    public static final long MAX_FLEETING_VISAGE_TICKS = 24000; // one minecraft day
    private final ServerPlayerEntity owner;
    private MaculaContent content = new MaculaContent();
    // "macula" dimensions (= client screen relative dimensions, defaulting to equivalent of 1920x1080 if a packet from the client doesn't come)
    // since this information is very volatile, it does not get persisted
    private int width = 480;
    private int height = 270;

    public Macula(ServerPlayerEntity owner) {
        this.owner = owner;
    }

    public MaculaContent getContent() {
        return content;
    }

    public void setContent(MaculaContent content) {
        this.content = content;
    }

    /**
     * It is normally recommended to use {@link #checkFullness()} instead of this
     * when checking at an arbitrary time, because checkFullness guarantees that only
     * non-outdated visages are counted towards the size, and this method does not guarantee that.
     *
     * @return whether the content is at or above {@link #MAX_VISAGES}.
     */
    public boolean isFull() {
        return content.size() >= MAX_VISAGES;
    }

    /**
     * Trims the content of outdated visages and checks {@link #isFull()}.
     *
     * @return whether the trimmed content is at or above {@link #MAX_VISAGES}.
     */
    public boolean checkFullness() {
        trim();
        return isFull();
    }

    public void add(VisageEntry entry) {
        if (entry.hasTimedOut(owner.getWorld().getTime())) return;
        content.add(entry);
    }

    public void trim() {
        content.removeIf(entry -> entry.hasTimedOut(owner.getWorld().getTime()));
    }

    public void clear() {
        content.clear();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
