package io.github.artynova.mediaworks.util;

import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class NbtUtils {
    public static NbtList serializeVec3d(Vec3d vec) {
        NbtList list = new NbtList();
        list.add(NbtDouble.of(vec.getX()));
        list.add(NbtDouble.of(vec.getY()));
        list.add(NbtDouble.of(vec.getZ()));
        return list;
    }

    public static Vec3d deserializeVec3d(NbtList list) {
        return new Vec3d(list.getDouble(0), list.getDouble(1), list.getDouble(2));
    }

    public static NbtList serializeVec3i(Vec3i vec) {
        NbtList list = new NbtList();
        list.add(NbtInt.of(vec.getX()));
        list.add(NbtInt.of(vec.getY()));
        list.add(NbtInt.of(vec.getZ()));
        return list;
    }

    public static Vec3i deserializeVec3i(NbtList list) {
        return new Vec3i(list.getInt(0), list.getInt(1), list.getInt(2));
    }
}
