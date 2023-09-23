package io.github.artynova.mediaworks.client.projection.camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.stat.StatHandler;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class AstralCameraEntity extends ClientPlayerEntity {
    public AstralCameraEntity(MinecraftClient client, ClientWorld world) {
        super(client, world, new FakeClientPlayNetworkHandler(client), new StatHandler(), new ClientRecipeBook(), false, false);

        getAbilities().flying = true;
        getAbilities().allowFlying = true;
        setInvisible(true);
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return player != client.player;
    }

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible();
    }

    @Nullable
    @Override
    public AbstractTeam getScoreboardTeam() {
        return null;
    }

    @Override
    public boolean isSpectator() {
        return true;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
