package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.api.client.macula.VisageRenderer;
import io.github.artynova.mediaworks.api.client.macula.VisageRenderers;
import io.github.artynova.mediaworks.api.logic.macula.Visage;
import io.github.artynova.mediaworks.api.logic.macula.VisageType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;

import java.util.Map;

public class VisageRendererLoader implements SynchronousResourceReloader {
    private static final VisageRendererLoader INSTANCE = new VisageRendererLoader();
    private static Map<VisageType<?>, VisageRenderer<?>> visageRenderers;

    private VisageRendererLoader() {
    }

    public static VisageRendererLoader getInstance() {
        return INSTANCE;
    }

    public static <T extends Visage> VisageRenderer<T> getRenderer(T visage) {
        if (visageRenderers == null)
            visageRenderers = VisageRenderers.loadVisageRendererMap(); // because forge for some reason does not fire the reload event on initial load
        return (VisageRenderer<T>) visageRenderers.get(visage.getType());
    }

    @Override
    public void reload(ResourceManager manager) {
        visageRenderers = VisageRenderers.loadVisageRendererMap();
    }
}
