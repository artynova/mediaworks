package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.macula.TextVisage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MediaworksVisageRenderers {
    public static void init() {
        VisageRenderers.register(TextVisage.TYPE, TextVisageRenderer::new);
    }
}
