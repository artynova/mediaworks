package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.api.client.macula.VisageRenderer;
import io.github.artynova.mediaworks.logic.macula.TextVisage;
import io.github.artynova.mediaworks.logic.macula.VisageEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TextVisageRenderer implements VisageRenderer<TextVisage> {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final TextRenderer TEXT_RENDERER = CLIENT.textRenderer;
    private static final int WHITE_COLOR = 0xFFFFFF;
    private static final int FADEOUT_TICKS = 20;
    private static final int MIN_RENDER_ALPHA = 9;

    @Override
    public Prepared prepare(VisageEntry entry) {
        return new PreparedTextVisageRenderer(entry);
    }

    private static class PreparedTextVisageRenderer implements Prepared {
        private final int x;
        private final int y;
        private final long endTime;
        private final List<OrderedText> lines;
        private int baseColor = WHITE_COLOR;

        private PreparedTextVisageRenderer(VisageEntry entry) {
            if (!(entry.getVisage() instanceof TextVisage visage)) {
                throw new IllegalArgumentException("Passed a non-TextVisage VisageEntry to TextVisageRenderer");
            }

            x = entry.getOrigin().getX();
            y = entry.getOrigin().getY();
            endTime = entry.getEndTime();

            // compute the lines
            Text text = visage.getText();

            int maxWidth = visage.getWidth();
            if (maxWidth == -1) maxWidth = TEXT_RENDERER.getWidth(text);
            List<OrderedText> tempLines = TEXT_RENDERER.wrapLines(text, maxWidth);

            int maxHeight = visage.getHeight();
            int maxLines = Math.min(tempLines.size(), maxHeight == -1 ? tempLines.size() : maxHeight / TEXT_RENDERER.fontHeight);
            lines = tempLines.subList(0, maxLines);
        }

        @Override
        public void render(MatrixStack matrixStack, float tickDelta) {
            if (!updateBaseColor()) return;
            int rowY = y;
            for (OrderedText orderedText : lines) {
                TEXT_RENDERER.drawWithShadow(matrixStack, orderedText, x, rowY, baseColor);
                rowY += TEXT_RENDERER.fontHeight;
            }
        }

        /**
         * Updates the base text color to apply transparency when fading out.
         *
         * @return whether the text should render (due to how minecraft accepts both rgb and argb works, a fully transparent argb color wraps around to being an opaque rgb color, so this distinction is necessary)
         */
        private boolean updateBaseColor() {
            baseColor = WHITE_COLOR;
            if (endTime < 0) return true;
            assert CLIENT.world != null;
            long remainingTime = endTime - CLIENT.world.getTime();
            if (remainingTime > FADEOUT_TICKS) return true;

            int alpha = (int) (remainingTime * 255.0f / FADEOUT_TICKS);
            alpha = MathHelper.clamp(alpha, 0, 255);
            if (alpha < MIN_RENDER_ALPHA) return false;
            int argbAlpha = alpha << 24;
            baseColor = argbAlpha | WHITE_COLOR;
            return true;
        }

        @Override
        public boolean doneDisplaying() {
            assert CLIENT.world != null;
            return endTime > -1 && endTime < CLIENT.world.getTime();
        }
    }
}
