package io.github.artynova.mediaworks.fabric.cc;

import at.petrak.hexcasting.api.spell.iota.Iota;
import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.artynova.mediaworks.projection.AstralPosition;
import io.github.artynova.mediaworks.projection.ProjectionDataSerializer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AstralProjectionComp implements Component {
    private final ServerPlayerEntity owner;
    private AstralPosition position;
    private Iota iota;
    private Vec3d origin;

    public AstralProjectionComp(ServerPlayerEntity owner) {
        this.owner = owner;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        position = ProjectionDataSerializer.getPlayerAstralPosition(tag);
        iota = ProjectionDataSerializer.getPlayerAstralIota(tag, owner.getWorld());
        origin = ProjectionDataSerializer.getPlayerAstralOrigin(tag);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        ProjectionDataSerializer.putPlayerAstralPosition(tag, position);
        ProjectionDataSerializer.putPlayerAstralIota(tag, iota);
        ProjectionDataSerializer.putPlayerAstralOrigin(tag, origin);
    }

    public @Nullable AstralPosition getPosition() {
        return position;
    }

    public void setPosition(@Nullable AstralPosition data) {
        this.position = data;
    }

    public @Nullable Iota getIota() {
        return iota;
    }

    public void setIota(@Nullable Iota iota) {
        this.iota = iota;
    }

    public @Nullable Vec3d getOrigin() {
        return origin;
    }

    public void setOrigin(@Nullable Vec3d origin) {
        this.origin = origin;
    }
}
