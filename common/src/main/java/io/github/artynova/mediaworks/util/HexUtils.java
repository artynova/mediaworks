package io.github.artynova.mediaworks.util;

import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import io.github.artynova.mediaworks.casting.ExtendedCastingContext;
import io.github.artynova.mediaworks.enchantment.LocaleMagnificationEnchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HexUtils {
    public static final double DEFAULT_AMBIT = 32.0;

    public static double getAmbitRadius(PlayerEntity player) {
        return DEFAULT_AMBIT + LocaleMagnificationEnchantment.getAmbitIncrease(player);
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

    public static Vec3d smartRaycast(double ambit, @NotNull Vec3d origin, @NotNull Vec3d look) {
        return origin.add(look.normalize().multiply(ambit));
    }

    /**
     * Wraps the cast from {@link CastingContext} to {@link ExtendedCastingContext} because we know
     * the cast is always possible, even if the IDE doesn't.
     */
    public static ExtendedCastingContext extend(CastingContext castingContext) {
        return (ExtendedCastingContext) (Object) castingContext;
    }
}
