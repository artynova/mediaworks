package io.github.artynova.mediaworks.fabric;

import at.petrak.hexcasting.api.spell.iota.Iota;
import io.github.artynova.mediaworks.fabric.cc.MediaworksCardinalComponents;
import io.github.artynova.mediaworks.projection.AstralPosition;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class MediaworksAbstractionsImpl {
    public static void setAstralPosition(ServerPlayerEntity player, @Nullable AstralPosition data) {
        MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).setPosition(data);
    }

    public static @Nullable AstralPosition getAstralPosition(ServerPlayerEntity player) {
        return MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).getPosition();
    }

    public static void setAstralIota(ServerPlayerEntity player, @Nullable Iota iota) {
        MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).setIota(iota);
    }

    public static @Nullable Iota getAstralIota(ServerPlayerEntity player) {
        return MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).getIota();
    }

    public static void setAstralOrigin(ServerPlayerEntity player, @Nullable Vec3d position) {
        MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).setOrigin(position);
    }

    public static @Nullable Vec3d getAstralOrigin(ServerPlayerEntity player) {
        return MediaworksCardinalComponents.ASTRAL_PROJECTION.get(player).getOrigin();
    }
}
