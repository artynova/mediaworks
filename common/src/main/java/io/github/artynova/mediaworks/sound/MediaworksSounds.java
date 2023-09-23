package io.github.artynova.mediaworks.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.artynova.mediaworks.api.MediaworksAPI;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import static io.github.artynova.mediaworks.api.MediaworksAPI.id;

public class MediaworksSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(MediaworksAPI.MOD_ID, Registry.SOUND_EVENT_KEY);
    public static final RegistrySupplier<SoundEvent> PROJECTION_AMBIANCE = SOUNDS.register("astral.ambience", () -> new SoundEvent(id("astral.ambience")));
    public static final RegistrySupplier<SoundEvent> PROJECTION_RETURN = SOUNDS.register("astral.return", () -> new SoundEvent(id("astral.return")));

    public static void init() {
        SOUNDS.register();
    }
}
