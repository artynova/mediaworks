package io.github.artynova.mediaworks.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.TriFunction;

public class MathHelpers {
    public static float slowdownInterpolationProgress(float point, float leftBound, float rightBound, float growthCoeff) {
        return (float) (1 - Math.pow(growthCoeff, -((point - leftBound) / (rightBound - leftBound))));
    }

    public static TriFunction<Float, Float, Float, Float> slowdownInterpolationProgressWithCoeff(float growthCoeff) {
        return (p, l, r) -> MathHelpers.slowdownInterpolationProgress(p, l, r, growthCoeff);
    }

    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
}
