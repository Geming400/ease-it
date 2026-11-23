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
public final class Animation<T extends Number> {
    /// The duration in ticks of this {@link Animation}
    private final int duration;
    /// The progress in ticks {@link Animation}
    /// @see #duration
    private int progress;

    private final BaseEasing<T> easing;
    private final EasingType easingType;
    private final ClickableWidget widget;
    private final Action<T> action;
    @Nullable
    private AnimationTickCallback<T> callback = null;

    public Animation(
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

    public Animation(
            ClickableWidget widget,
            BaseEasing<T> easing,
            EasingType easingType,
            Duration duration,
            Action<T> action
    ) {
       this(widget, easing, easingType, durationToTicks(duration), action);
    }

    /// @see #getProgress()
    public int getDuration() {
        return this.duration;
    }

    /// @see #getProgress()
    public int getTickProgress() {
        return this.progress;
    }

    /**
     * Gets the progress of this {@link Animation}
     * @return the progress of this {@link Animation} between {@code [0, 1]}
     * @see #getDuration()
     * @see #getTickProgress()
     */
    @Range(from = 0, to = 1)
    public double getProgress() {
        return (double) this.progress / this.duration;
    }

    public boolean isFinished() {
        return this.progress >= this.duration;
    }

    public BaseEasing<T> getEasing() {
        return this.easing;
    }

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
    public AnimationTickCallback<T> getTickCallback() {
        return this.callback;
    }

    /**
     * Sets the callback to be executed each time this {@link Animation} {@linkplain #tick() ticks}
     * @param callback the callback
     * @return {@code this}
     */
    public Animation<T> setCallback(@Nullable AnimationTickCallback<T> callback) {
        this.callback = callback;
        return this;
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
        if (!(o instanceof Animation<?> animation)) return false;
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

    public static Animation<Integer> ofInt(
            ClickableWidget widget,
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<Integer> action
    ) {
        return new Animation<>(
                widget,
                easing.asIntEasing(),
                easingType,
                duration,
                action
        );
    }

    public static Animation<Float> ofFloat(
            ClickableWidget widget,
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<Float> action
    ) {
        return new Animation<>(
                widget,
                easing.asFloatEasing(),
                easingType,
                duration,
                action
        );
    }
    public static Animation<Double> ofDouble(
            ClickableWidget widget,
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            Action<Double> action
    ) {
        return new Animation<>(
                widget,
                easing.asDoubleEasing(),
                easingType,
                duration,
                action
        );
    }

    private static int durationToTicks(Duration duration) {
        return Math.toIntExact(duration.toSeconds() * 20);
    }

    public interface AnimationTickCallback<T extends Number> {
        void onTick(
                Animation<T> animation,
                ClickableWidget widget,
                BaseEasing.FullEasingFunction<T> easingFunction,
                @Range(from = 0, to = 1) double progress
        );
    }
}
