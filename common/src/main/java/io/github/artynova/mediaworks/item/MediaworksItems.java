package io.github.artynova.mediaworks.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.artynova.mediaworks.Mediaworks;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class MediaworksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Mediaworks.MOD_ID, Registry.ITEM_KEY);
    public static final RegistrySupplier<MagicCloakItem> MAGIC_CLOAK = ITEMS.register("magic_cloak", MagicCloakItem::new);


    public static void init() {
        ITEMS.register();
    }
}
