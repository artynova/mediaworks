package io.github.artynova.mediaworks.client.macula;

import io.github.artynova.mediaworks.logic.macula.TextVisage;
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
    @Override
    public Prepared<TextVisage> prepare(TextVisage visage) {
        return new PreparedTextVisageRenderer(visage);
    }

    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final TextRenderer textRenderer = CLIENT.textRenderer;

    private static class PreparedTextVisageRenderer implements Prepared<TextVisage> {
        private final TextVisage visage;
        private final int x;
        private final int y;
        private final List<OrderedText> lines;

        private PreparedTextVisageRenderer(TextVisage visage) {
            this.visage = visage;

            x = Math.round(visage.getOrigin().x);
            y = Math.round(visage.getOrigin().y);
            int maxWidth = Math.round(visage.getDimensions().x);
            int maxHeight = Math.round(visage.getDimensions().y);

            Text text = visage.getText();
            if (visage.isBounded()) {
                // wrap text horizontally, and limit lines to only those that fully fit by using integer division
                int maxLines = maxHeight / textRenderer.fontHeight;
                List<OrderedText> temp = textRenderer.wrapLines(text, maxWidth);
                lines = temp.subList(0, Math.min(temp.size(), maxLines));
            }
            else lines = List.of(text.asOrderedText());
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
            return visage.hasTimedOut(CLIENT.world.getTime());
        }
    }
}
