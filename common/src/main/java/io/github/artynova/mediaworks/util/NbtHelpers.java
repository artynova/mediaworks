package io.github.artynova.mediaworks.util;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec2f;

public class NbtHelpers {
    public static NbtList serializeVec2(Vec2f vec) {
        NbtList list = new NbtList();
        list.add(NbtFloat.of(vec.x));
        list.add(NbtFloat.of(vec.y));
        return list;
    }

    public static Vec2f deserializeVec2(NbtList list) {
        return new Vec2f(list.getFloat(0), list.getFloat(1));
    }
}
