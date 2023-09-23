package io.github.artynova.mediaworks.fabric;

import dev.architectury.event.events.common.LifecycleEvent;
import io.github.artynova.mediaworks.MediaworksClient;
import io.github.artynova.mediaworks.fabric.client.armor.MagicCloakRenderer;
import io.github.artynova.mediaworks.item.MagicCloakItem;
import io.github.artynova.mediaworks.item.MediaworksItems;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.entity.EntityRenderers;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MediaworksClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MediaworksClient.init();
        MediaworksItems.MAGIC_CLOAK.listen(cloak -> GeoArmorRenderer.registerArmorRenderer(new MagicCloakRenderer(), cloak));
    }
}
