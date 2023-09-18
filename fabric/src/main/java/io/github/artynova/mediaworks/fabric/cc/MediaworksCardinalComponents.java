package io.github.artynova.mediaworks.fabric.cc;

import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import at.petrak.hexcasting.fabric.cc.adimpl.CCMediaHolder;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import io.github.artynova.mediaworks.interop.supplementaries.SackMediaHolder;
import io.github.artynova.mediaworks.interop.supplementaries.SupplementariesInterop;
import io.github.artynova.mediaworks.misc.ShulkerBoxMediaHolder;
import net.minecraft.server.network.ServerPlayerEntity;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksCardinalComponents implements EntityComponentInitializer, ItemComponentInitializer {
    public static final ComponentKey<ProjectionHolderComp> PROJECTION_HOLDER = ComponentRegistry.getOrCreate(id("projection_holder"), ProjectionHolderComp.class);
    public static final ComponentKey<MaculaHolderComp> MACULA_HOLDER = ComponentRegistry.getOrCreate(id("macula_holder"), MaculaHolderComp.class);
    public static final ComponentKey<CCMediaHolder> MEDIA_HOLDER = HexCardinalComponents.MEDIA_HOLDER;

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // persistent projection data is only needed on the server side, because the client knows all about it from the camera
        registry.registerFor(ServerPlayerEntity.class, PROJECTION_HOLDER, ProjectionHolderComp::new);
        // persistent macula data is only stored on the server: because it is needed every tick for rendering on the client, it is stored directly in a more readily available way
        registry.registerFor(ServerPlayerEntity.class, MACULA_HOLDER, MaculaHolderComp::new);
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(ShulkerBoxMediaHolder::isShulkerBox, MEDIA_HOLDER, stack -> new ContainerItemMediaHolderComp(new ShulkerBoxMediaHolder(stack)));
        // don't need to register this in the first place if supplementaries is not present
        if (SupplementariesInterop.isPresent())
            registry.register(SackMediaHolder::isSack, MEDIA_HOLDER, stack -> new ContainerItemMediaHolderComp(new SackMediaHolder(stack)));
    }
}
