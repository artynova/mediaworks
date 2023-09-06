package io.github.artynova.mediaworks.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerHelpers {
    public static List<ServerPlayerEntity> playersNear(Vec3d pos, double radius, ServerWorld serverWorld, Predicate<ServerPlayerEntity> extraTest) {
        double radiusSquared = radius * radius;
        return serverWorld.getPlayers()
                .stream()
                .filter((player) -> player.squaredDistanceTo(pos) <= radiusSquared && extraTest.test(player))
                .collect(Collectors.toList());
    }

    public static List<ServerPlayerEntity> playersNear(Vec3d pos, double radius, ServerWorld serverWorld) {
        return playersNear(pos, radius, serverWorld, player -> false); // include all players
    }
}
