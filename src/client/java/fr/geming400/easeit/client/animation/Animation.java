package fr.geming400.easeit.client.animation;

import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.animation.easing.EasingType;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface Animation<T extends Number> {
    /// @see #getProgress()
    int getTickProgress();

    /// @see #getProgress()
    int getDuration();

    /**
     * Gets the progress of this {@link WidgetAnimation}
     * @return the progress of this {@link WidgetAnimation} between {@code [0, 1]}
     * @see #getDuration()
     * @see #getTickProgress()
     */
    @Range(from = 0, to = 1)
    default double getProgress() {
        return (double) this.getTickProgress() / this.getDuration();
    }

    default boolean isFinished() {
        return this.getProgress() >= this.getDuration();
    }

    BaseEasing<T> getEasing();

    EasingType getEasingType();

    AnimationTickCallback<T> getTickCallback();

    Class<T> getNumberType();

    /**
     * Sets the callback to be executed each time this {@link WidgetAnimation} {@linkplain #tick() ticks}
     * @param callback the callback
     */
    void setCallback(@Nullable AnimationTickCallback<T> callback);

    void tick();


    interface AnimationTickCallback<T extends Number> {
        void onTick(
                WidgetAnimation<T> animation,
                ClickableWidget widget,
                BaseEasing.FullEasingFunction<T> easingFunction,
                @Range(from = 0, to = 1) double progress
        );
    }
}
