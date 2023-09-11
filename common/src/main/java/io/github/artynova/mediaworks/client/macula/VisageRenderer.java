package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.macula.Visage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value= EnvType.CLIENT)

public interface VisageRenderer<T extends Visage> {
    Prepared<T> prepare(T visage);

    interface Prepared<T extends Visage> {
        void render(MatrixStack stack);

        /**
         * @return true if the visage should no longer be rendered, and thus the renderer is safe to dispose of.
         */
        boolean doneDisplaying();
    }
}
