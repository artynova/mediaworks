package io.github.artynova.mediaworks.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.MediaworksAbstractions;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class MediaworksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Mediaworks.MOD_ID, Registry.ITEM_KEY);
    public static final RegistrySupplier<ArmorItem> MAGIC_CLOAK = ITEMS.register("magic_cloak", MediaworksAbstractions::makeMagicCloakItem);


    public static void init() {
        ITEMS.register();

        MagicCloakItem.initPackagedHexDiscovery();

        // make cloaks washable in cauldrons
        MediaworksItems.MAGIC_CLOAK.listen(item -> CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(item, CauldronBehavior.CLEAN_DYEABLE_ITEM));
    }
}
