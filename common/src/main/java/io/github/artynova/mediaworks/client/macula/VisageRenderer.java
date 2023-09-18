package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.logic.macula.Visage;
import io.github.artynova.mediaworks.logic.macula.VisageEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)

public interface VisageRenderer<T extends Visage> {
    /**
     * @param entry a visage entry that must contain a visage of type {@link T}.
     * @return an object that has preemptively done all computations necessary for rendering the passed {@link VisageEntry}, to avoid doing them in every render.
     */
    Prepared prepare(VisageEntry entry);

    interface Prepared {
        void render(MatrixStack stack);

        /**
         * @return true if the visage should no longer be rendered, and thus the renderer is safe to dispose of.
         */
        boolean doneDisplaying();
    }
}
