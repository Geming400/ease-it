package fr.geming400.easeit.client.animation;

import fr.geming400.easeit.client.animation.easing.BaseEasing;
import fr.geming400.easeit.client.animation.easing.EasingType;
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

    void tick();
}
