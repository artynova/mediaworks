package io.github.artynova.mediaworks.projection;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.ParticleSpray;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.ControllerInfo;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.architectury.event.EventResult;
import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.effect.MediaworksEffects;
import io.github.artynova.mediaworks.networking.AstralPositionSyncS2CMsg;
import io.github.artynova.mediaworks.networking.EndProjectionS2CMsg;
import io.github.artynova.mediaworks.networking.MediaworksNetworking;
import io.github.artynova.mediaworks.networking.SpawnHexParticlesS2CMsg;
import io.github.artynova.mediaworks.sound.MediaworksSounds;
import io.github.artynova.mediaworks.util.HexHelpers;
import io.github.artynova.mediaworks.util.PlayerHelpers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstralProjectionServer {
    public static final int NAUSEA_TICKS = 200;
    public static final int NAUSEA_AMPLIFIER = 4;
    public static final double INITIAL_HEIGHT_OFFSET = 0.5;
    public static final int PARTICLE_COUNT_PER_TICK = 3;
    public static final int CAST_COOLDOWN_TICKS = 10;
    public static final double SQUARED_BODY_MOVEMENT_LIMIT = 9.0;

    private static final Map<ServerPlayerEntity, Integer> COOLDOWN_CACHE = new HashMap<>();

    public static void syncFromClient(ServerPlayerEntity player, AstralPosition position) {
        if (!isProjectingOnServer(player)) return;
        MediaworksAbstractions.setAstralPosition(player, position);

        // check if the camera has left the ambit
        if (!HexHelpers.isInAmbit(position.coordinates(), player)) {
            endProjectionAbruptly(player);
            return;
        }

        // spawn particles for everybody nearby, except the dissociated player
        Vec3d eyePos = new Vec3d(position.coordinates().getX(), position.coordinates().getY() + player.getStandingEyeHeight(), position.coordinates().getZ());
        ParticleSpray spray = ParticleSpray.cloud(eyePos, 1.0, PARTICLE_COUNT_PER_TICK);
        FrozenColorizer color = IXplatAbstractions.INSTANCE.getColorizer(player);
        List<ServerPlayerEntity> players = PlayerHelpers.playersNear(spray.getPos(), 128.0, player.getWorld(), otherPlayer -> otherPlayer != player);
        MediaworksNetworking.sendToPlayers(players, new SpawnHexParticlesS2CMsg(spray, color));
    }

    public static void syncToClient(ServerPlayerEntity player, AstralPosition position) {
        MediaworksNetworking.sendToPlayer(player, new AstralPositionSyncS2CMsg(position));
    }

    public static boolean isProjectingOnServer(ServerPlayerEntity player) {
        return MediaworksAbstractions.getAstralPosition(player) != null;
    }

    /**
     * Starts the projection state. Does not consider the buff.
     */
    public static void startProjection(ServerPlayerEntity player) {
        if (!isProjectingOnServer(player)) {
            MediaworksAbstractions.setAstralOrigin(player, player.getPos());
            AstralPosition position = new AstralPosition(new Vec3d(player.getX(), player.getY() + INITIAL_HEIGHT_OFFSET, player.getZ()), player.getHeadYaw(), player.getPitch());
            syncToClient(player, position);
            MediaworksAbstractions.setAstralPosition(player, position);
        }
    }

    /**
     * Ends the projection state. Does not consider the buff.
     */
    public static void endProjection(ServerPlayerEntity player) {
        MediaworksNetworking.sendToPlayer(player, new EndProjectionS2CMsg());
        MediaworksAbstractions.setAstralPosition(player, null);
        MediaworksAbstractions.setAstralIota(player, null);
        MediaworksAbstractions.setAstralOrigin(player, null);
        COOLDOWN_CACHE.remove(player);
        player.playSound(MediaworksSounds.PROJECTION_RETURN.get(), SoundCategory.PLAYERS, 1f, 1f);
    }

    /**
     * Removes the projection buff.
     * If the buff was present, this triggers removal of client-side projection as a normal response to buff removal.
     */
    public static void endProjectionEarly(ServerPlayerEntity player) {
        player.removeStatusEffect(MediaworksEffects.ASTRAL_PROJECTION.get());
    }

    /**
     * Ends projection early and applies a nausea effect.
     */
    public static void endProjectionAbruptly(ServerPlayerEntity player) {
        endProjectionEarly(player);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, NAUSEA_TICKS, NAUSEA_AMPLIFIER));
    }

    /**
     * Evaluates the astral iota captured from ravenmind, if any.
     */
    public static void evaluateIota(ServerPlayerEntity player) {
        if (COOLDOWN_CACHE.getOrDefault(player, 0) > 0) return;
        COOLDOWN_CACHE.put(player, CAST_COOLDOWN_TICKS);
        IXplatAbstractions.INSTANCE.clearCastingData(player);
        CastingHarness harness = IXplatAbstractions.INSTANCE.getHarness(player, Hand.MAIN_HAND);
        Iota iota = MediaworksAbstractions.getAstralIota(player);
        if (iota == null) return; // missing astral iota = non-op
        ControllerInfo outcome;
        if (iota instanceof ListIota listIota) {
            outcome = harness.executeIotas(HexHelpers.decompose(listIota), player.getWorld());
        } else {
            outcome = harness.executeIota(iota, player.getWorld());
        }
        IXplatAbstractions.INSTANCE.clearCastingData(player);
        // terminate after encountering an issue
        if (!outcome.component2().getSuccess()) endProjectionAbruptly(player);
    }

    public static void handlePlayerTick(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (!isProjectingOnServer(serverPlayer)) return;

        // check if the body has moved from casting point too much
        if (player.getPos().squaredDistanceTo(MediaworksAbstractions.getAstralOrigin(serverPlayer)) > SQUARED_BODY_MOVEMENT_LIMIT) {
            endProjectionAbruptly(serverPlayer);
        }
        // tick cooldown
        COOLDOWN_CACHE.put(serverPlayer, Math.max(COOLDOWN_CACHE.getOrDefault(serverPlayer, 0) - 1, 0));
    }

    /**
     * If the player has stored astral projection information (was projecting when quit),
     * makes the client start projection with camera synchronized to its recorded data.
     */
    public static void handleJoin(ServerPlayerEntity player) {
        AstralPosition position = MediaworksAbstractions.getAstralPosition(player);
        if (position != null) {
            syncToClient(player, position);
        }
    }

    public static EventResult handleDeath(LivingEntity entity, DamageSource source) {
        if (!(entity instanceof ServerPlayerEntity serverPlayer)) return EventResult.pass();
        endProjection(serverPlayer);
        return EventResult.pass();
    }

    /**
     * Ends the Astral Projection state if the player body manages to change dimensions.
     */
    public static void handleDimensionChange(ServerPlayerEntity player, RegistryKey<World> previous, RegistryKey<World> current) {
        if (!isProjectingOnServer(player)) return;
        endProjectionAbruptly(player);
    }
}
