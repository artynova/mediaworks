package io.github.artynova.mediaworks;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.artynova.mediaworks.item.MagicCloakItem;
import io.github.artynova.mediaworks.logic.macula.Macula;
import io.github.artynova.mediaworks.logic.projection.AstralProjection;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

// Static helper methods that depend on the mod loader in some way.
public class MediaworksAbstractions {
    @ExpectPlatform
    public static @NotNull AstralProjection getProjection(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @NotNull Macula getMacula(@NotNull ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void initLoaderSpecificInterop() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static EnchantmentTarget getCloakEnchantmentTarget() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ArmorItem makeMagicCloakItem() {
        throw new AssertionError();
    }
}
