package fr.geming400.easeit.client.animation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.animation.easing.EasingType;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.Nullable;
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
    @Nullable
    private Animation.AnimationTickCallback<T> callback = null;
    private final Class<T> numberType;

    public WidgetAnimation(
            ClickableWidget widget,
            BaseEasing<T> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<T> action,
            Class<T> numberType
    ) {
        this.duration = duration;
        this.progress = 0;
        this.easing = easing;
        this.easingType = easingType;
        this.widget = widget;
        this.action = action;
        this.numberType = numberType;
    }

    public WidgetAnimation(
            ClickableWidget widget,
            BaseEasing<T> easing,
            EasingType easingType,
            Duration duration,
            Action<T> action,
            Class<T> numberType
    ) {
       this(widget, easing, easingType, durationToTicks(duration), action, numberType);
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

    @Nullable
    @Override
    public Animation.AnimationTickCallback<T> getTickCallback() {
        return this.callback;
    }

    @Override
    public Class<T> getNumberType() {
        return this.numberType;
    }

    public void setCallback(@Nullable Animation.AnimationTickCallback<T> callback) {
        this.callback = callback;
    }

    public void tick() {
        if (!this.isFinished()) {
            this.progress += 1;

            BaseEasing.FullEasingFunction<T> easingFunction = BaseEasing.FullEasingFunction.of(this.easing, this.easingType);
            this.action.tick(this.widget, this, easingFunction, this.getProgress());
            if (this.callback != null)
                this.callback.onTick(this, widget, easingFunction, this.getProgress());
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
                action,
                Integer.class
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
                action,
                Float.class
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
                action,
                Double.class
        );
    }

    private static int durationToTicks(Duration duration) {
        return Math.toIntExact(duration.toSeconds() * 20);
    }
}
