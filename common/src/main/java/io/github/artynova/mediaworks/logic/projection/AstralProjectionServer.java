package io.github.artynova.mediaworks.logic.projection;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.ParticleSpray;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.ControllerInfo;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.architectury.event.EventResult;
import io.github.artynova.mediaworks.MediaworksAbstractions;
import io.github.artynova.mediaworks.logic.DataCache;
import io.github.artynova.mediaworks.effect.MediaworksEffects;
import io.github.artynova.mediaworks.networking.projection.SyncAstralPositionS2CMsg;
import io.github.artynova.mediaworks.networking.projection.EndProjectionS2CMsg;
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

import java.util.List;

public class AstralProjectionServer {
    public static final int NAUSEA_TICKS = 200;
    public static final int NAUSEA_AMPLIFIER = 4;
    public static final double INITIAL_HEIGHT_OFFSET = 0.5;
    public static final int PARTICLE_COUNT_PER_TICK = 3;
    public static final int CAST_COOLDOWN_TICKS = 10;
    public static final double SQUARED_BODY_MOVEMENT_LIMIT = 9.0;

    private static final DataCache<ServerPlayerEntity, AstralProjection> PROJECTION_CACHE = new DataCache<>(player -> {
        AstralProjection projection = MediaworksAbstractions.getProjection(player);
        projection.setCooldown(0);
        return projection;
    });


    public static void syncFromClient(ServerPlayerEntity player, AstralPosition position) {
        if (!isProjecting(player)) return;
        getProjection(player).setPosition(position);

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
        MediaworksNetworking.sendToPlayer(player, new SyncAstralPositionS2CMsg(position));
    }

    public static boolean isProjecting(ServerPlayerEntity player) {
        return getProjection(player).isActive();
    }

    public static AstralProjection getProjection(ServerPlayerEntity player) {
        return PROJECTION_CACHE.getOrCompute(player);
    }

    /**
     * Starts the projection state. Does not consider the buff.
     */
    public static void startProjection(ServerPlayerEntity player) {
        if (!isProjecting(player)) {
            AstralProjection projection = getProjection(player);
            projection.setOrigin(player.getPos());
            AstralPosition position = new AstralPosition(new Vec3d(player.getX(), player.getY() + INITIAL_HEIGHT_OFFSET, player.getZ()), player.getHeadYaw(), player.getPitch());
            projection.setPosition(position);
            syncToClient(player, position);
        }
    }

    /**
     * Ends the projection state. Does not consider the buff.
     */
    public static void endProjection(ServerPlayerEntity player) {
        MediaworksNetworking.sendToPlayer(player, new EndProjectionS2CMsg());
        getProjection(player).end();
        PROJECTION_CACHE.remove(player);
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
        AstralProjection projection = getProjection(player);
        if (projection.getCooldown() > 0) return;
        projection.setCooldown(CAST_COOLDOWN_TICKS);
        IXplatAbstractions.INSTANCE.clearCastingData(player);
        CastingHarness harness = IXplatAbstractions.INSTANCE.getHarness(player, Hand.MAIN_HAND);
        Iota iota = projection.getIota();
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
        AstralProjection projection = getProjection(serverPlayer);
        if (!projection.isActive()) return;

        // check if the body has moved from casting point too much
        if (player.getPos().squaredDistanceTo(projection.getOrigin()) > SQUARED_BODY_MOVEMENT_LIMIT) {
            endProjectionAbruptly(serverPlayer);
        }
        projection.tickCooldown();
    }

    /**
     * If the player has stored astral projection information (was projecting when quit),
     * makes the client start projection with camera synchronized to its recorded data.
     */
    public static void handleJoin(ServerPlayerEntity player) {
        AstralProjection projection = getProjection(player);
        if (projection.getPosition() != null) {
            syncToClient(player, projection.getPosition());
        }
    }

    /**
     * Removes the player's projection data from the cache when quitting.
     */
    public static void handleQuit(ServerPlayerEntity player) {
        PROJECTION_CACHE.remove(player);
    }

    /**
     * Clears projection of the old player if cloning happens due to leaving the end.
     * Needed because this does not get picked up by the dimension change event.
     */
    public static void handleClone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) {
        if (wonGame) {
            endProjectionAbruptly(oldPlayer);
            newPlayer.playSound(MediaworksSounds.PROJECTION_RETURN.get(), SoundCategory.PLAYERS, 1f, 1f);

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
        if (!isProjecting(player)) return;
        endProjectionAbruptly(player);
    }
}
