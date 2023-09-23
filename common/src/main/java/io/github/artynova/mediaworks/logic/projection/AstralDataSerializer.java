package io.github.artynova.mediaworks.logic.projection;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import io.github.artynova.mediaworks.util.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

/**
 * Helper class to abstract from serialization code in the actual {@link AstralProjection}.
 */
public class AstralDataSerializer {
    public static final String ASTRAL_POSITION_TAG = "astral_pos";
    public static final String ASTRAL_IOTA_TAG = "astral_iota";
    public static final String ASTRAL_ORIGIN_TAG = "astral_origin";

    public static void putPlayerAstralPosition(NbtCompound compound, @Nullable AstralPosition position) {
        if (position == null) {
            compound.remove(ASTRAL_POSITION_TAG);
            return;
        }
        compound.put(ASTRAL_POSITION_TAG, AstralPosition.serialize(position));
    }

    public static @Nullable AstralPosition getPlayerAstralPosition(NbtCompound compound) {
        if (!compound.contains(ASTRAL_POSITION_TAG)) return null;
        return AstralPosition.deserialize(compound.getCompound(ASTRAL_POSITION_TAG));
    }

    public static void putPlayerAstralIota(NbtCompound compound, @Nullable Iota iota) {
        if (iota == null) {
            compound.remove(ASTRAL_IOTA_TAG);
            return;
        }
        compound.put(ASTRAL_IOTA_TAG, HexIotaTypes.serialize(iota));
    }

    public static @Nullable Iota getPlayerAstralIota(NbtCompound compound, ServerWorld world) {
        if (!compound.contains(ASTRAL_IOTA_TAG)) return null;
        return HexIotaTypes.deserialize(compound.getCompound(ASTRAL_IOTA_TAG), world);
    }

    public static void putPlayerAstralOrigin(NbtCompound playerData, @Nullable Vec3d pos) {
        if (pos == null) {
            playerData.remove(ASTRAL_ORIGIN_TAG);
            return;
        }
        playerData.put(ASTRAL_ORIGIN_TAG, NbtUtils.serializeVec3d(pos));
    }

    public static @Nullable Vec3d getPlayerAstralOrigin(NbtCompound playerData) {
        if (!playerData.contains(ASTRAL_ORIGIN_TAG)) return null;
        NbtList list = playerData.getList(ASTRAL_ORIGIN_TAG, NbtElement.DOUBLE_TYPE);
        return NbtUtils.deserializeVec3d(list);
    }
}
