package fr.geming400.easeit.client.animation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fr.geming400.easeit.client.Utils;
import fr.geming400.easeit.client.animation.animatable.GlobalAnimatable;
import fr.geming400.easeit.client.animation.animatable.HudAnimatable;
import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.animation.easing.EasingType;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.Range;

import java.time.Duration;

/**
 * Represents an animation playable by an {@link HudAnimatable} to render dynamic HUD elements.
 * To get a {@link HudAnimatable} instance, use {@link GlobalAnimatable#getHud()}
 */
public final class HudAnimation<T extends Number> implements Animation<T> {
    private final int duration;
    private int progress;

    private final BaseEasing<T> easing;
    private final EasingType easingType;
    private final AnimationTickCallback<T> animationTickCallback;

    public HudAnimation(
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

    public HudAnimation(
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

    public void tick(DrawContext drawContext) {
        if (!this.isFinished()) {
            this.progress += 1;

            BaseEasing.FullEasingFunction<T> easingFunction = BaseEasing.FullEasingFunction.of(this.easing, this.easingType);
            this.animationTickCallback.onTick(drawContext, this, this.getProgress(), easingFunction);
        }
    }

    @Override
    public void tick() {
        throw new UnsupportedOperationException("You must call HudAnimation#tick(DrawContext) tick overload instead !");
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
        if (!(o instanceof HudAnimation<?> animation)) return false;
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

    public static HudAnimation<Integer> ofInt(
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<Integer> onTick
    ) {
        return new HudAnimation<>(
                easing.asIntEasing(),
                easingType,
                duration,
                onTick
        );
    }

    public static HudAnimation<Float> ofFloat(
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<Float> onTick
    ) {
        return new HudAnimation<>(
                easing.asFloatEasing(),
                easingType,
                duration,
                onTick
        );
    }
    public static HudAnimation<Double> ofDouble(
            BaseEasing<?> easing,
            EasingType easingType,
            @Range(from = 0, to = Integer.MAX_VALUE) int duration,
            AnimationTickCallback<Double> onTick
    ) {
        return new HudAnimation<>(
                easing.asDoubleEasing(),
                easingType,
                duration,
                onTick
        );
    }

    @FunctionalInterface
    public interface AnimationTickCallback<T extends Number> {
        void onTick(DrawContext drawContext, HudAnimation<T> animation, double progress, BaseEasing.FullEasingFunction<T> easingFunction);
    }
}
