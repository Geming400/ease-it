package fr.geming400.easeit.client.animation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.geming400.easeit.client.Utils;
import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.animation.easing.EasingType;
import org.jetbrains.annotations.Range;

import java.time.Duration;

/**
 * Represents an animation playable by an {@link Animatable} widget, aka <b>any widget</b>
 */
public final class SimpleAnimation<T extends Number> implements Animation<T> {
    /// The duration in ticks of this {@link SimpleAnimation}
    private final int duration;
    /// The progress in ticks {@link SimpleAnimation}
    /// @see #duration
    private int progress;

    private final BaseEasing<T> easing;
    private final EasingType easingType;
    private final AnimationTickCallback<T> animationTickCallback;

    public SimpleAnimation(
            BaseEasing<T> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<T> animationTickCallback
    ) {
        this.duration = duration;
        this.progress = 0;
        this.easing = easing;
        this.easingType = easingType;
        this.animationTickCallback = animationTickCallback;
    }

    public SimpleAnimation(
            BaseEasing<T> easing,
            EasingType easingType,
            Duration duration,
            AnimationTickCallback<T> animationTickCallback
    ) {
       this(easing, easingType, Utils.durationToTicks(duration), animationTickCallback);
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

    public AnimationTickCallback<T> getTickCallback() {
        return this.animationTickCallback;
    }

    public void tick() {
        if (!this.isFinished()) {
            this.progress += 1;

            BaseEasing.FullEasingFunction<T> easingFunction = BaseEasing.FullEasingFunction.of(this.easing, this.easingType);
            this.animationTickCallback.onTick(this, this.getProgress(), easingFunction);
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
        if (!(o instanceof SimpleAnimation<?> animation)) return false;
        return this.duration == animation.duration
                && this.progress == animation.progress
                && Objects.equal(this.easing, animation.easing)
                && this.easingType == animation.easingType
                && this.animationTickCallback == animation.animationTickCallback;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.easing, this.easingType, this.animationTickCallback);
    }

    public static SimpleAnimation<Integer> ofInt(
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<Integer> onTick
    ) {
        return new SimpleAnimation<>(
                easing.asIntEasing(),
                easingType,
                duration,
                onTick
        );
    }

    public static SimpleAnimation<Float> ofFloat(
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<Float> onTick
    ) {
        return new SimpleAnimation<>(
                easing.asFloatEasing(),
                easingType,
                duration,
                onTick
        );
    }
    public static SimpleAnimation<Double> ofDouble(
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<Double> onTick
    ) {
        return new SimpleAnimation<>(
                easing.asDoubleEasing(),
                easingType,
                duration,
                onTick
        );
    }

    @FunctionalInterface
    public interface AnimationTickCallback<T extends Number> {
        void onTick(SimpleAnimation<T> animation, double progress, BaseEasing.FullEasingFunction<T> easingFunction);
    }
}
