package io.github.artynova.mediaworks.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.artynova.mediaworks.Mediaworks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Mediaworks.MOD_ID)
public class MediaworksForge {
    public MediaworksForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Mediaworks.MOD_ID, bus);
        bus.addListener(MediaworksClientForge::init);
        Mediaworks.init();
    }
}
