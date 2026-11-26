package fr.geming400.easeit.client.animation;

import fr.geming400.easeit.client.animation.easing.BaseEasing;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public final class ActionHelpers {
    private ActionHelpers() {}

    public static Vector2ic moveHelper(Vector2ic from, Vector2ic to, BaseEasing.FullEasingFunction<Integer> easing, double progress) {
        return new Vector2i(
                easing.get(progress, from.x(), to.x()),
                easing.get(progress, from.y(), to.y())
        );
    }

    public static Vector2ic relativeMoveHelper(Vector2ic from, Vector2ic relativePos, BaseEasing.FullEasingFunction<Integer> easing, double progress) {
        Vector2i absoluteEndPos = from.add(relativePos, new Vector2i());

        return new Vector2i(
                easing.get(progress, from.x(), absoluteEndPos.x()),
                easing.get(progress, from.y(), absoluteEndPos.y())
        );
    }

    public static Vector2ic resizeHelper(Vector2ic startSize, Vector2ic endSize, BaseEasing.FullEasingFunction<Integer> easing, double progress) {
        return new Vector2i(
                easing.get(progress, startSize.x(), endSize.x()),
                easing.get(progress, startSize.y(), endSize.y())
        );
    }

    public static float fadeHelper(float startAlpha, float endAlpha, BaseEasing.FullEasingFunction<Float> easing, double progress) {
        return easing.get(progress, Math.clamp(startAlpha, 0, 1), Math.clamp(endAlpha, 0, 1));
    }
}
