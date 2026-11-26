package fr.geming400.easeit.client.animation;

import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.mixin.ClickableWidgetAccessor;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.Range;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents an action executable by an {@link WidgetAnimation}.
 * <p>
 * To store states used in actions, use {@linkplain AtomicReference atomic references} <i>(ex: {@link #createMove(Vector2ic)})</i>
 */
@FunctionalInterface
public interface Action<T extends Number> {
    static Action<Integer> createMove(Vector2ic to) {
        AtomicReference<Vector2ic> originalWidgetPos = new AtomicReference<>();
        return (widget, animation, easing, progress) -> {
            if (originalWidgetPos.get() == null)
                originalWidgetPos.set(new Vector2i(widget.getX(), widget.getY()));

            widget.setPosition(
                    easing.get(progress, originalWidgetPos.get().x(), to.x()),
                    easing.get(progress, originalWidgetPos.get().y(), to.y())
            );

            if (animation.isFinished())
                originalWidgetPos.set(null);
        };
    }

    static Action<Integer> createRelativeMove(Vector2ic relativeEndPos) {
        AtomicReference<Vector2ic> originalWidgetPos = new AtomicReference<>();
        return (widget, animation, easing, progress) -> {
            if (originalWidgetPos.get() == null)
                originalWidgetPos.set(new Vector2i(widget.getX(), widget.getY()));

            Vector2i absoluteEndPos = originalWidgetPos.get().add(relativeEndPos, new Vector2i());

            widget.setPosition(
                    easing.get(progress, originalWidgetPos.get().x(), absoluteEndPos.x()),
                    easing.get(progress, originalWidgetPos.get().y(), absoluteEndPos.y())
            );

            if (animation.isFinished())
                originalWidgetPos.set(null);
        };
    }

    static Action<Integer> createResize(int width, int height) {
        AtomicReference<Integer> originalWidgetWidth = new AtomicReference<>(null);
        AtomicReference<Integer> originalWidgetHeight = new AtomicReference<>(null);
        return (widget, animation, easing, progress) -> {
            if (originalWidgetWidth.get() == null)
                originalWidgetWidth.set(widget.getWidth());

            if (originalWidgetHeight.get() == null)
                originalWidgetHeight.set(widget.getHeight());

            widget.setDimensions(easing.get(progress, originalWidgetWidth.get(), width), easing.get(progress, originalWidgetHeight.get(), height));
        };
    }

    static Action<Float> createAlpha(@Range(from = 0, to = 1) float from, @Range(from = 0, to = 1) float to) {
        float clampedFrom = Math.clamp(from, 0, 1);
        float clampedTo = Math.clamp(to, 0, 1);

        return (widget, animation, easing, progress) ->
                widget.setAlpha(easing.get(progress, clampedFrom, clampedTo));
    }

    static Action<Float> createAlpha(@Range(from = 0, to = 1) float to) {
        float clampedTo = Math.clamp(to, 0, 1);
        AtomicReference<Float> originalWidgetAlpha = new AtomicReference<>(null);

        return (widget, animation, easing, progress) -> {
            if (originalWidgetAlpha.get() == null) {
                float alpha = ((ClickableWidgetAccessor) widget).getAlpha();
                originalWidgetAlpha.set(Math.clamp(alpha, 0, 1));
            }

            widget.setAlpha(easing.get(progress, originalWidgetAlpha.get(), clampedTo));
        };
    }

    static <T extends Number> Action<T> empty() {
        return (widget, animation, easing, progress) -> {};
    }

    /**
     * Gets called whenever the {@link WidgetAnimation} ticks
     * @param widget the {@link ClickableWidget} affected by the {@link Action}
     * @param animation the current {@link WidgetAnimation}
     * @param easing the {@link BaseEasing.EasingFunction} used to apply the easing at the given {@code progress}
     * @param progress the "progress" value is in the range {@code [0, 1]} and describes how long has the
     */
    void tick(ClickableWidget widget, WidgetAnimation<T> animation, BaseEasing.FullEasingFunction<T> easing, @Range(from = 0, to = 1) double progress);
    default void tick(WidgetAnimation<T> animation, BaseEasing.FullEasingFunction<T> easing) {
        this.tick(animation.getWidget(), animation, easing, animation.getProgress());
    }
}
