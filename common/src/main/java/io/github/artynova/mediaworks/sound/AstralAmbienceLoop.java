package io.github.artynova.mediaworks.sound;

import io.github.artynova.mediaworks.client.projection.AstralProjectionClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class AstralAmbienceLoop extends MovingSoundInstance {
    private final ClientPlayerEntity player;

    public AstralAmbienceLoop(ClientPlayerEntity player) {
        super(MediaworksSounds.PROJECTION_AMBIANCE.get(), SoundCategory.AMBIENT, SoundInstance.createRandom());
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.0f;
        this.relative = true;
    }

    @Override
    public void tick() {
        if (this.player.isRemoved() || !AstralProjectionClient.isDissociated()) {
            this.setDone();
        }
    }

    @Override
    public boolean isDone() {
        return super.isDone();
    }
}
