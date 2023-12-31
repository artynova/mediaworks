package io.github.artynova.mediaworks.api.client.macula;

import io.github.artynova.mediaworks.api.logic.macula.Visage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface VisageRendererFactory<T extends Visage> {
    VisageRenderer<T> create();
}
