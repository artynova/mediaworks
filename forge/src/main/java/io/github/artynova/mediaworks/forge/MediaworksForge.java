package io.github.artynova.mediaworks.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.artynova.mediaworks.Mediaworks;
import io.github.artynova.mediaworks.api.MediaworksAPI;
import io.github.artynova.mediaworks.forge.capabilities.MediaworksCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MediaworksAPI.MOD_ID)
public class MediaworksForge {
    public MediaworksForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        EventBuses.registerModEventBus(MediaworksAPI.MOD_ID, modBus);
        modBus.addListener(MediaworksClientForge::init);
        modBus.addListener(MediaworksClientForge::registerArmorRenderer);
        Mediaworks.init();


        modBus.addListener(MediaworksCapabilities::registerCaps);
        eventBus.addGenericListener(Entity.class, MediaworksCapabilities::attachEntityCaps);
        eventBus.addGenericListener(ItemStack.class, MediaworksCapabilities::attachItemCaps);
    }
}
