package io.github.artynova.mediaworks.client.util;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.ParticleSpray;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.artynova.mediaworks.mixin.projection.WorldRendererAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class RenderUtils {
    private static final Random RANDOM = new Random();

    public static void renderPlayerEntity(PlayerEntity player, MatrixStack matrixStack, WorldRenderer worldRenderer, Camera camera, float tickDelta, VertexConsumerProvider consumers) {
        matrixStack.push();
        player.lastRenderX = player.prevX;
        player.lastRenderY = player.prevY;
        player.lastRenderZ = player.prevZ;
        ((WorldRendererAccessor) worldRenderer).callRenderEntity(player, camera.getPos().x, camera.getPos().y, camera.getPos().z, tickDelta, matrixStack, consumers);
        matrixStack.pop();
    }

    public static void drawRect(MatrixStack matrixStack, float x, float y, float width, float height, int color) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        builder.vertex(matrix, x, y, 0f).color(color).next();
        builder.vertex(matrix, x, y + height, 0f).color(color).next();
        builder.vertex(matrix, x + width, y + height, 0f).color(color).next();
        builder.vertex(matrix, x + width, y, 0f).color(color).next();
        tessellator.draw();
    }

    /**
     * @param point         number that represents proximity of the color to rightColor, between 0 and rightBound.
     * @param leftBound     least acceptable value for point.
     * @param rightBound    max acceptable value for point.
     * @param leftColor     color when point is 0.
     * @param rightColor    color when point is rightBound.
     * @param deltaFunction function that returns custom interpolation delta between 0 and 1 based on point, left, and right bounds.
     * @return color with its RGBA components linearly interpolated between leftColor's and rightColor's
     */
    public static int interpolateColor(float point, float leftBound, float rightBound, int leftColor, int rightColor, TriFunction<Float, Float, Float, Float> deltaFunction) {
        point = Math.min(Math.max(point, leftBound), rightBound);
        float progress = deltaFunction.apply(point, leftBound, rightBound);

        int alpha = (int) (MathHelper.lerp(progress, (leftColor >> 24) & 0xFF, (rightColor >> 24) & 0xFF));
        int red = (int) (MathHelper.lerp(progress, (leftColor >> 16) & 0xFF, (rightColor >> 16) & 0xFF));
        int green = (int) (MathHelper.lerp(progress, (leftColor >> 8) & 0xFF, (rightColor >> 8) & 0xFF));
        int blue = (int) (MathHelper.lerp(progress, leftColor & 0xFF, rightColor & 0xFF));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    // The following code is verbatim from Hex Casting, used to reproduce their casting particles with more control
    public static void renderHexParticleSpray(ParticleSpray spray, FrozenColorizer colorizer) {
        for (int i = 0; i < spray.getCount(); i++) {
            // For the colors, pick any random time to get a mix of colors
            var color = colorizer.getColor(RANDOM.nextFloat() * 256f, Vec3d.ZERO);

            var offset = randomInCircle(MathHelper.TAU).normalize()
                    .multiply(RANDOM.nextFloat() * spray.getFuzziness() / 2);
            var pos = spray.getPos().add(offset);

            var phi = Math.acos(1.0 - RANDOM.nextDouble() * (1.0 - Math.cos(spray.getSpread())));
            var theta = Math.PI * 2.0 * RANDOM.nextDouble();
            var v = spray.getVel().normalize();
            // pick any old vector to get a vector normal to v with
            Vec3d k;
            if (v.x == 0.0 && v.y == 0.0) {
                // oops, pick a *different* normal
                k = new Vec3d(1.0, 0.0, 0.0);
            } else {
                k = v.crossProduct(new Vec3d(0.0, 0.0, 1.0));
            }
            var velUnlen = v.multiply(Math.cos(phi))
                    .add(k.multiply(Math.sin(phi) * Math.cos(theta)))
                    .add(v.crossProduct(k).multiply(Math.sin(phi) * Math.sin(theta)));
            var vel = velUnlen.multiply(spray.getVel().length() / 20);

            MinecraftClient.getInstance().world.addParticle(
                    new ConjureParticleOptions(color, false),
                    pos.x, pos.y, pos.z,
                    vel.x, vel.y, vel.z
            );
        }
    }

    private static Vec3d randomInCircle(double maxTh) {
        var th = RANDOM.nextDouble(0.0, maxTh + 0.001);
        var z = RANDOM.nextDouble(-1.0, 1.0);
        return new Vec3d(Math.sqrt(1.0 - z * z) * Math.cos(th), Math.sqrt(1.0 - z * z) * Math.sin(th), z);
    }
}
