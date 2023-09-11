package io.github.artynova.mediaworks.logic.projection;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.logic.PersistentDataContainer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AstralProjection implements PersistentDataContainer {
    private final ServerPlayerEntity owner;
    private @Nullable AstralPosition position;
    private @Nullable Vec3d origin;
    private @Nullable Iota iota;
    /**
     * Cast cooldown, not serialized.
     * When projection is not ongoing, the cooldown is taken as {@code 0}.
     */
    private int cooldown = 0;

    public AstralProjection(ServerPlayerEntity owner) {
        this.owner = owner;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        position = AstralDataSerializer.getPlayerAstralPosition(tag);
        if (position == null) return; // not active = no point deserializing further
        origin = AstralDataSerializer.getPlayerAstralOrigin(tag);
        iota = AstralDataSerializer.getPlayerAstralIota(tag, owner.getWorld());
        cooldown = 0;
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        AstralDataSerializer.putPlayerAstralPosition(tag, position);
        AstralDataSerializer.putPlayerAstralOrigin(tag, origin);
        AstralDataSerializer.putPlayerAstralIota(tag, iota);
    }

    public boolean isActive() {
        return position != null;
    }

    /**
     * Clears all current projection data, if there is any.
     */
    public void end() {
        position = null;
        origin = null;
        iota = null;
        cooldown = 0;
    }

    public @Nullable AstralPosition getPosition() {
        return position;
    }

    public void setPosition(@Nullable AstralPosition position) {
        this.position = position;
    }

    public @Nullable Vec3d getOrigin() {
        return origin;
    }

    public void setOrigin(@Nullable Vec3d origin) {
        this.origin = origin;
    }

    public @Nullable Iota getIota() {
        return iota;
    }

    public void setIota(@Nullable Iota iota) {
        this.iota = iota;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void tickCooldown() {
        this.cooldown = Math.max(this.cooldown - 1, 0);
    }
}
