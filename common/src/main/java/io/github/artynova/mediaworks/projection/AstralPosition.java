package io.github.artynova.mediaworks.projection;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public record AstralPosition(Vec3d coordinates, float yaw, float pitch) {
    public static final String COORDS_TAG = "coords";
    public static final String YAW_TAG = "yaw";
    public static final String PITCH_TAG = "pitch";

    public static NbtCompound serialize(AstralPosition data) {
        NbtCompound compound = new NbtCompound();
        NbtList coords = new NbtList();
        coords.add(NbtDouble.of(data.coordinates().getX()));
        coords.add(NbtDouble.of(data.coordinates().getY()));
        coords.add(NbtDouble.of(data.coordinates().getZ()));
        compound.put(COORDS_TAG, coords);
        compound.putFloat(YAW_TAG, data.yaw);
        compound.putFloat(PITCH_TAG, data.pitch);
        return compound;
    }

    public static AstralPosition deserialize(@NotNull NbtCompound compound) {
        NbtList coordsList = compound.getList(COORDS_TAG, NbtElement.DOUBLE_TYPE);
        Vec3d coords = new Vec3d(coordsList.getDouble(0), coordsList.getDouble(1), coordsList.getDouble(2));
        float yaw = compound.getFloat(YAW_TAG);
        float pitch = compound.getFloat(PITCH_TAG);
        return new AstralPosition(coords, yaw, pitch);
    }
}
