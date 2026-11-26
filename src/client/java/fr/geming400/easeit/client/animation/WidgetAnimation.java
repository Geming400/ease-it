package fr.geming400.easeit.client.animation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.geming400.easeit.client.Utils;
import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.animation.easing.EasingType;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.Range;

import java.time.Duration;

/**
 * Represents an animation playable by an {@link Animatable} widget, aka <b>any widget</b>
 */
public final class WidgetAnimation<T extends Number> implements Animation<T> {
    /// The duration in ticks of this {@link WidgetAnimation}
    private final int duration;
    /// The progress in ticks {@link WidgetAnimation}
    /// @see #duration
    private int progress;

    private final BaseEasing<T> easing;
    private final EasingType easingType;
    private final ClickableWidget widget;
    private final Action<T> action;

    public WidgetAnimation(
            ClickableWidget widget,
            BaseEasing<T> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<T> action
    ) {
        this.duration = duration;
        this.progress = 0;
        this.easing = easing;
        this.easingType = easingType;
        this.widget = widget;
        this.action = action;
    }

    public WidgetAnimation(
            ClickableWidget widget,
            BaseEasing<T> easing,
            EasingType easingType,
            Duration duration,
            Action<T> action
    ) {
       this(widget, easing, easingType, Utils.durationToTicks(duration), action);
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public int getTickProgress() {
        return this.progress;
    }

    @Range(from = 0, to = 1)
    @Override
    public double getProgress() {
        return (double) this.progress / this.duration;
    }

    @Override
    public boolean isFinished() {
        return this.progress >= this.duration;
    }

    @Override
    public BaseEasing<T> getEasing() {
        return this.easing;
    }

    @Override
    public EasingType getEasingType() {
        return this.easingType;
    }

    public ClickableWidget getWidget() {
        return this.widget;
    }

    public Action<T> getAction() {
        return this.action;
    }

    public void tick() {
        if (!this.isFinished()) {
            this.progress += 1;

            BaseEasing.FullEasingFunction<T> easingFunction = BaseEasing.FullEasingFunction.of(this.easing, this.easingType);
            this.action.tick(this.widget, this, easingFunction, this.getProgress());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("progress", this.progress)
                .add("duration", this.duration)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WidgetAnimation<?> animation)) return false;
        return this.duration == animation.duration
                && this.progress == animation.progress
                && Objects.equal(this.easing, animation.easing)
                && this.easingType == animation.easingType
                && Objects.equal(this.widget, animation.widget)
                && Objects.equal(this.action, animation.action);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.widget, this.easing, this.easingType);
    }

    public static WidgetAnimation<Integer> ofInt(
            ClickableWidget widget,
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<Integer> action
    ) {
        return new WidgetAnimation<>(
                widget,
                easing.asIntEasing(),
                easingType,
                duration,
                action
        );
    }

    public static WidgetAnimation<Float> ofFloat(
            ClickableWidget widget,
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<Float> action
    ) {
        return new WidgetAnimation<>(
                widget,
                easing.asFloatEasing(),
                easingType,
                duration,
                action
        );
    }
    public static WidgetAnimation<Double> ofDouble(
            ClickableWidget widget,
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<Double> action
    ) {
        return new WidgetAnimation<>(
                widget,
                easing.asDoubleEasing(),
                easingType,
                duration,
                action
        );
    }
}
