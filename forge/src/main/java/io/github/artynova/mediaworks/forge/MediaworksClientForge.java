package io.github.artynova.mediaworks.forge;

import io.github.artynova.mediaworks.MediaworksClient;
import io.github.artynova.mediaworks.forge.client.armor.MagicCloakRenderer;
import io.github.artynova.mediaworks.forge.item.MagicCloakItemImpl;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MediaworksClientForge {
    public static void init(FMLClientSetupEvent event) {
        MediaworksClient.init();

    }

    public static void registerArmorRenderer(EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(MagicCloakItemImpl.class, MagicCloakRenderer::new);
    }
}
