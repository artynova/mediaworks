package io.github.artynova.mediaworks.effect;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.artynova.mediaworks.Mediaworks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;

public class MediaworksEffects {
    public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(Mediaworks.MOD_ID, Registry.MOB_EFFECT_KEY);
    public static final RegistrySupplier<StatusEffect> ASTRAL_PROJECTION = EFFECTS.register("astral_projection", AstralProjectionEffect::new);

    public static void init() {
        EFFECTS.register();
    }
}
