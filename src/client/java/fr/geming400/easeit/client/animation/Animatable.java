package fr.geming400.easeit.client.animation;

import fr.geming400.easeit.client.animation.easing.Easing;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import org.jetbrains.annotations.ApiStatus;

import java.util.Deque;
import java.util.List;

/**
 * An {@link Animatable} {@link Widget} can be "animated" using the {@link Easing} system and {@linkplain Action actions}.
 * To animate a widget use {@link #easeit$addAnimation(Animation)} and its overload, or the {@link #easeit$addAnimations(List)} variant.
 * <p>
 * {@linkplain Animatable Animatable} widgets can concurrently play {@linkplain Animation animations}.
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
 *             20 * 5, // 5 seconds (or 100 ticks)
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
    default List<Animation<?>> easeit$getAnimations() {
        throw new AssertionError("Implemented in Mixin");
    }

    /**
     * Adds multiple {@linkplain Animation animations} onto the {@linkplain #easeit$getAnimations() queue} and plays them if possible
     * @param animations the {@linkplain Animation animations} to add
     * @return the added {@linkplain Animation animations}
     */
    default List<Animation<?>> easeit$addAnimations(List<Animation<?>> animations) {
        this.easeit$getAnimations().addAll(animations);
        return animations;
    }

    /**
     * Adds multiple {@linkplain Animation animations} onto the {@linkplain #easeit$getAnimations() queue} and plays them if possible
     * @param animations the {@linkplain Animation animations} to add
     * @return the added {@linkplain Animation animations}
     */
    default List<Animation<?>> easeit$addAnimations(Animation<?>... animations) {
        List<Animation<?>> asList = List.of(animations);

        this.easeit$getAnimations().addAll(asList);
        return asList;
    }

    /**
     * Adds an {@link Animation} onto the {@linkplain #easeit$getAnimations() queue} and plays it if possible
     * @param animation the {@link Animation} to add
     * @return the added {@link Animation}
     * @see #easeit$addAnimation(Animation, Animation.AnimationTickCallback)
     */
    default <T extends Number> Animation<T> easeit$addAnimation(Animation<T> animation) {
        this.easeit$getAnimations().add(animation);
        return animation;
    }

    /**
     * Adds an {@link Animation} onto the {@linkplain #easeit$getAnimations() queue} and plays it if possible
     * @param animation the {@link Animation} to add
     * @param tickCallback the callback to call whenever the given {@link Animation} ticks
     * @return the added {@link Animation}
     * @see #easeit$addAnimation(Animation)
     */
    default <T extends Number> Animation<T> easeit$addAnimation(Animation<T> animation, Animation.AnimationTickCallback<T> tickCallback) {
        return this.easeit$addAnimation(animation.setCallback(tickCallback));
    }


    /**
     * This is called every time the widget ticks thanks to mixins.
     * This is used to tick every {@linkplain Animation animations}
     * @see Animation#tick
     */
    default void easeit$tickWidgetAnimations() {
        List<Animation<?>> animations = this.easeit$getAnimations();
        animations.forEach(Animation::tick);
        animations.stream()
                .filter(Animation::isFinished)
                .forEach(animation ->
                    this.easeit$getAnimations().remove(animation)
                );
    }
}
