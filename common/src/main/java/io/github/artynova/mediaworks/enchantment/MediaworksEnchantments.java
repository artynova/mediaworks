package io.github.artynova.mediaworks.enchantment;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.MediaworksAbstractions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class MediaworksEnchantments {
    public static final EnchantmentTarget CLOAK_TARGET = MediaworksAbstractions.getCloakEnchantmentTarget();
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Mediaworks.MOD_ID, Registry.ENCHANTMENT_KEY);
    public static final RegistrySupplier<Enchantment> RECIPROCATION = ENCHANTMENTS.register("reciprocation", ReciprocationEnchantment::new);
    public static final RegistrySupplier<Enchantment> MEDIA_SHIELD = ENCHANTMENTS.register("media_shield", MediaShieldEnchantment::new);
    public static final RegistrySupplier<Enchantment> LOCALE_MAGNIFICATION = ENCHANTMENTS.register("locale_magnification", LocaleMagnificationEnchantment::new);

    public static void init() {
        ENCHANTMENTS.register();

        // add enchantments to combat group
        EnchantmentTarget[] oldTargets = ItemGroup.COMBAT.getEnchantments();
        EnchantmentTarget[] targets = Arrays.copyOf(oldTargets, oldTargets.length + 1);
        targets[oldTargets.length] = CLOAK_TARGET;
        ItemGroup.COMBAT.setEnchantments(targets);
    }
}
