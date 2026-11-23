package fr.geming400.easeit.client.animation;

import fr.geming400.easeit.client.animation.easing.Easing;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import oshi.annotation.concurrent.Immutable;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * An {@link Animatable} {@link Widget} can be "animated" using the {@link Easing} system and {@linkplain Action actions}.
 * To animate a widget use {@link #easeit$addAnimation(Animation)},
 * the other functions are not needed for this task.
 * <p>
 * Example:
 * <code>{@snippet lang=java:
 * class MyScreen extends Screen {
 *     // ctor, ...
 *
 *     protected void init() {
 *         IconWidget iconWidget = IconWidget.create(10, 10, ICON_TEXTURE);
 *         iconWidget.easeit$addAnimation(Animation.ofInt(
 *             iconWidget,
 *             Easing.EXPONENTIAL,
 *             EasingType.OUT,
 *             Utils.durationToTicks(Duration.ofSeconds(3)),
 *             Action.createResize(50, 50))
 *         );
 *     }
 * }}</code>
 * <p><br>
 * This gets injected automatically to {@link ClickableWidget ClickableWidgets} and {@link Screen Screens} using interface injection
 */
@ApiStatus.NonExtendable
public interface Animatable {
    /**
     * Gets this {@link Widget}'s {@linkplain Animation animations}
     * @return a mutable {@link Deque} containing this {@link Widget}'s {@linkplain Animation animations}
     */
    default Deque<Animation<?>> easeit$getAnimationsQueue() {
        throw new AssertionError("Implemented in Mixin");
    }

    /**
     * Gets all the animations of this {@link Widget}
     * @return an immutable list containing all the animations of this {@link Widget}, including the {@linkplain #easeit$getCurrentAnimation() playing one}
     */
    @Immutable
    default List<Animation<?>> easeit$getAllAnimations() {
        List<Animation<?>> animations = new ArrayList<>(this.easeit$getAnimationsQueue());
        if (this.easeit$getCurrentAnimation() != null)
            animations.add(this.easeit$getCurrentAnimation());

        return List.copyOf(animations);
    }

    /**
     * Sets the current playing {@link Animation}. Can be {@code null} to stop the current animation from playing by removing it
     * @param animation the {@link Animation} to play
     * @see #easeit$popAnimationQueue()
     * @see #easeit$addAnimation(Animation)
     */
    default void easeit$setCurrentAnimation(@Nullable Animation<?> animation) {
        throw new AssertionError("Implemented in Mixin");
    }

    /**
     * Gets the current playing {@link Animation}. Is {@code null} if there's none playing at the moment
     * @return Gets the current playing {@link Animation}
     */
    @Nullable
    default Animation<?> easeit$getCurrentAnimation() {
        throw new AssertionError("Implemented in Mixin");
    }

    /**
     * Pops the next {@link Animation} from the {@linkplain #easeit$getAnimationsQueue() queue} to play it
     * @return the {@link Animation} that will get played, or {@code null} if none can be played
     * @see #easeit$getCurrentAnimation()
     */
    @Nullable
    default Animation<?> easeit$popAnimationQueue() {
        Animation<?> animation = this.easeit$getAnimationsQueue().pollLast();
        this.easeit$setCurrentAnimation(animation);
        return animation;
    }

    /**
     * Adds an {@link Animation} onto the {@linkplain #easeit$getAnimationsQueue() queue} and plays it if possible
     * @param animation the {@link Animation} to add
     * @return the added {@link Animation}
     */
    default <T extends Number> Animation<T> easeit$addAnimation(Animation<T> animation) {
        this.easeit$getAnimationsQueue().add(animation);
        // If there's only the animation we added onto the queue
        if (this.easeit$getCurrentAnimation() == null)
            this.easeit$popAnimationQueue();

        return animation;
    }

    /**
     * Adds an {@link Animation} onto the {@linkplain #easeit$getAnimationsQueue() queue} and plays it if possible
     * @param animation the {@link Animation} to add
     * @param tickCallback the callback to call whenever the given {@link Animation} ticks
     * @return the added {@link Animation}
     */
    default <T extends Number> Animation<T> easeit$addAnimation(Animation<T> animation, Animation.AnimationTickCallback<T> tickCallback) {
        return this.easeit$addAnimation(animation.setCallback(tickCallback));
    }


    /**
     * This is called every time the widget ticks thanks to mixins.
     * This is used to tick ever {@linkplain Animation animations}
     */
    default void easeit$tickWidgetAnimations() {
        Animation<?> currentAnimation = this.easeit$getCurrentAnimation();
        if (currentAnimation != null) {
            currentAnimation.tick();

            // If it's finished then we can move on
            // To the next animation in queue
            // What if other longer animations are pushed on the stack ?
            if (currentAnimation.isFinished())
                this.easeit$popAnimationQueue();
        }
    }
}
