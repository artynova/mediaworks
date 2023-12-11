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
    private static final int MIN_RENDER_ALPHA = 9;

    @Override
    public Prepared prepare(VisageEntry entry) {
        return new PreparedTextVisageRenderer(entry);
    }

    private static class PreparedTextVisageRenderer implements Prepared {
        public static final long INERT_REMAINING_FADEOUT_TICKS = -1; // value for remainingFadeoutTicks specifying that the fadeout should not be occurring
        private final int x;
        private final int y;
        private final long endTime;
        private final boolean doFadeout;
        private final List<OrderedText> lines;
        /**
         * Value -1 specifies that fadeout is not occurring at the moment.
         * Value 0 specifies that the visage has already finished fading, even if it still logically exists.
         * Short-lived visages bypass the fading animation and go straight from -1 to 0.
         */
        private long remainingFadeoutTicks = INERT_REMAINING_FADEOUT_TICKS;

        private PreparedTextVisageRenderer(VisageEntry entry) {
            if (!(entry.getVisage() instanceof TextVisage visage)) {
                throw new IllegalArgumentException("Passed a non-TextVisage VisageEntry to TextVisageRenderer");
            }

            x = entry.getOrigin().getX();
            y = entry.getOrigin().getY();
            doFadeout = entry.doFadeout();
            endTime = entry.getEndTime();

            assert CLIENT.world != null;
            // if a visage with fadeout comes in already in the state of fading (when restoring macula on login),
            // consider the animation already finished
            if (doFadeout && endTime - CLIENT.world.getTime() <= VisageEntry.FADE_TICKS) remainingFadeoutTicks = 0;

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
            int baseColor = getBaseColor();
            if (doneDisplaying()) return;
            int rowY = y;
            for (OrderedText orderedText : lines) {
                TEXT_RENDERER.drawWithShadow(matrixStack, orderedText, x, rowY, baseColor);
                rowY += TEXT_RENDERER.fontHeight;
            }
        }

        @Override
        public void tick() {
            if (isFading()) remainingFadeoutTicks--;
        }

        /**
         * Updates the base text color to apply transparency when fading out.
         *
         * @return current base color (potentially transparent, if fading is being applied).
         */
        private int getBaseColor() {
            if (doneDisplaying()) return WHITE_COLOR; // return a dummy
            if (isLasting()) {
                resetFadeoutTicks();
                return WHITE_COLOR;
            }

            if (!isFading()) {
                assert CLIENT.world != null;
                long remainingTime = endTime - CLIENT.world.getTime();

                // case that bypasses the fade animation entirely (short-lived visages)
                if (!doFadeout) {
                    if (remainingTime > 0) resetFadeoutTicks();
                    else remainingFadeoutTicks = 0;
                    return WHITE_COLOR;
                }
                if (remainingTime > VisageEntry.FADE_TICKS) return WHITE_COLOR;

                // initiate fadeout sequence
                if (remainingFadeoutTicks == INERT_REMAINING_FADEOUT_TICKS)
                    remainingFadeoutTicks = VisageEntry.FADE_TICKS;
            }


            int alpha = (int) (remainingFadeoutTicks * 255.0f / VisageEntry.FADE_TICKS);
            alpha = MathHelper.clamp(alpha, 0, 255);
            if (alpha < MIN_RENDER_ALPHA) remainingFadeoutTicks = 0;
            int argbAlpha = alpha << 24;
            return argbAlpha | WHITE_COLOR;
        }

        private void resetFadeoutTicks() {
            remainingFadeoutTicks = INERT_REMAINING_FADEOUT_TICKS;
        }

        private boolean isFading() {
            return remainingFadeoutTicks > 0;
        }

        private boolean isLasting() {
            return endTime < 0;
        }

        @Override
        public boolean doneDisplaying() {
            assert CLIENT.world != null;
            return remainingFadeoutTicks == 0;
        }
    }
}
