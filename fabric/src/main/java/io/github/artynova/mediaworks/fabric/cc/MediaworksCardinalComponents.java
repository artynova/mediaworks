package io.github.artynova.mediaworks.fabric.cc;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.server.network.ServerPlayerEntity;

import static io.github.artynova.mediaworks.Mediaworks.id;

public class MediaworksCardinalComponents implements EntityComponentInitializer {
    public static final ComponentKey<AstralProjectionComp> ASTRAL_PROJECTION = ComponentRegistry.getOrCreate(id("astral_projection"), AstralProjectionComp.class);
    public static final ComponentKey<MaculaComp> MACULA = ComponentRegistry.getOrCreate(id("macula"), MaculaComp.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // persistent projection data is only needed on the server side, because the client knows all about it from the camera
        registry.registerFor(ServerPlayerEntity.class, ASTRAL_PROJECTION, AstralProjectionComp::new);
        // persistent macula data is only stored on the server: because it is needed every tick for rendering on the client, it is stored directly in a more readily available way
        registry.registerFor(ServerPlayerEntity.class, MACULA, MaculaComp::new);
    }
}
