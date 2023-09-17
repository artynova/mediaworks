package io.github.artynova.mediaworks.fabric.cc;

import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import io.github.artynova.mediaworks.logic.media.BEItemMediaHolder;
import io.github.artynova.mediaworks.misc.ShulkerBoxMediaHolder;
import net.minecraft.server.network.ServerPlayerEntity;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksCardinalComponents implements EntityComponentInitializer, ItemComponentInitializer {
    public static final ComponentKey<ProjectionHolderComp> PROJECTION_HOLDER = ComponentRegistry.getOrCreate(id("projection_holder"), ProjectionHolderComp.class);
    public static final ComponentKey<MaculaHolderComp> MACULA_HOLDER = ComponentRegistry.getOrCreate(id("macula_holder"), MaculaHolderComp.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // persistent projection data is only needed on the server side, because the client knows all about it from the camera
        registry.registerFor(ServerPlayerEntity.class, PROJECTION_HOLDER, ProjectionHolderComp::new);
        // persistent macula data is only stored on the server: because it is needed every tick for rendering on the client, it is stored directly in a more readily available way
        registry.registerFor(ServerPlayerEntity.class, MACULA_HOLDER, MaculaHolderComp::new);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(ShulkerBoxMediaHolder::isShulkerBox, HexCardinalComponents.MEDIA_HOLDER, stack -> new ContainerItemMediaHolderComp(new ShulkerBoxMediaHolder(stack)));
    }
}
