package io.github.artynova.mediaworks.projection;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class AstralDataSerializer {
    public static final String ASTRAL_POSITION_TAG = "mediaworks:astral_pos";
    public static final String ASTRAL_IOTA_TAG = "mediaworks:astral_iota";
    public static final String ASTRAL_ORIGIN_TAG = "mediaworks:astral_origin";

    public static void putPlayerAstralPosition(NbtCompound playerTags, @Nullable AstralPosition position) {
        if (position == null) {
            playerTags.remove(ASTRAL_POSITION_TAG);
            return;
        }
        playerTags.put(ASTRAL_POSITION_TAG, AstralPosition.serialize(position));
    }

    public static @Nullable AstralPosition getPlayerAstralPosition(NbtCompound playerData) {
        if (!playerData.contains(ASTRAL_POSITION_TAG)) return null;
        return AstralPosition.deserialize(playerData.getCompound(ASTRAL_POSITION_TAG));
    }

    public static void putPlayerAstralIota(NbtCompound playerData, @Nullable Iota iota) {
        if (iota == null) {
            playerData.remove(ASTRAL_IOTA_TAG);
            return;
        }
        playerData.put(ASTRAL_IOTA_TAG, HexIotaTypes.serialize(iota));
    }

    public static @Nullable Iota getPlayerAstralIota(NbtCompound playerData, ServerWorld world) {
        if (!playerData.contains(ASTRAL_IOTA_TAG)) return null;
        return HexIotaTypes.deserialize(playerData.getCompound(ASTRAL_IOTA_TAG), world);
    }

    public static void putPlayerAstralOrigin(NbtCompound playerData, @Nullable Vec3d pos) {
        if (pos == null) {
            playerData.remove(ASTRAL_ORIGIN_TAG);
            return;
        }
        NbtList list = new NbtList();
        list.add(NbtDouble.of(pos.getX()));
        list.add(NbtDouble.of(pos.getY()));
        list.add(NbtDouble.of(pos.getZ()));
        playerData.put(ASTRAL_ORIGIN_TAG, list);
    }

    public static @Nullable Vec3d getPlayerAstralOrigin(NbtCompound playerData) {
        if (!playerData.contains(ASTRAL_ORIGIN_TAG)) return null;
        NbtList list = playerData.getList(ASTRAL_ORIGIN_TAG, NbtElement.DOUBLE_TYPE);
        return new Vec3d(list.getDouble(0), list.getDouble(1), list.getDouble(2));
    }
}
