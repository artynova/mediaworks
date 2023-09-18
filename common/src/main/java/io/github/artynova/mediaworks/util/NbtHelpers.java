package io.github.artynova.mediaworks.util;

import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3i;

public class NbtHelpers {
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
