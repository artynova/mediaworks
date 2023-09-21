package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.logic.macula.TextVisage;
import io.github.artynova.mediaworks.logic.macula.VisageEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TextVisageRenderer implements VisageRenderer<TextVisage> {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final TextRenderer textRenderer = CLIENT.textRenderer;

    @Override
    public Prepared prepare(VisageEntry entry) {
        return new PreparedTextVisageRenderer(entry);
    }

    private static class PreparedTextVisageRenderer implements Prepared {
        private final int x;
        private final int y;
        private final long endTime;
        private final List<OrderedText> lines;

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
            if (maxWidth == -1) maxWidth = textRenderer.getWidth(text);
            List<OrderedText> tempLines = textRenderer.wrapLines(text, maxWidth);

            int maxHeight = visage.getHeight();
            int maxLines = Math.min(tempLines.size(), maxHeight == -1 ? tempLines.size() : maxHeight / textRenderer.fontHeight);
            lines = tempLines.subList(0, maxLines);
        }

        @Override
        public void render(MatrixStack matrixStack) {
            int rowY = y;
            for (OrderedText orderedText : lines) {
                textRenderer.drawWithShadow(matrixStack, orderedText, x, rowY, 0xFF_FFFFFF);
                rowY += textRenderer.fontHeight;
            }
        }

        @Override
        public boolean doneDisplaying() {
            assert CLIENT.world != null;
            return endTime > -1 && endTime < CLIENT.world.getTime();
        }
    }
}
