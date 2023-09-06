package io.github.artynova.mediaworks.util;

import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class HexHelpers {
    public static double getAmbitRadius(PlayerEntity player) {
        return 32; // TODO replace with actual calculations when the enchantment is available
    }

    /**
     * Helper method to find whether a vector is or is not within caster's ambit
     * without needing to create a new casting context.
     */
    public static boolean isInAmbit(Vec3d vec, ServerPlayerEntity caster) {
        if (vec.squaredDistanceTo(caster.getEyePos()) <= MathHelper.square(getAmbitRadius(caster))) return true;
        Sentinel sentinel = IXplatAbstractions.INSTANCE.getSentinel(caster);
        return sentinel.hasSentinel() && sentinel.extendsRange() && caster.getWorld().getRegistryKey().equals(sentinel.dimension()) && vec.squaredDistanceTo(sentinel.position()) < 256.0;
    }

    public static List<Iota> decompose(ListIota listIota) {
        List<Iota> res = new ArrayList<>();
        for (Iota iota : listIota.getList()) res.add(iota);
        return res;
    }
}
