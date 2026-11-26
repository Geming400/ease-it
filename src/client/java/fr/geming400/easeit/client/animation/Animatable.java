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
 * To animate a widget use {@link #easeit$addAnimation(T)} and its overload, or the {@link #easeit$addAnimations(List)} variant.
 * <p>
 * {@linkplain Animatable Animatable} widgets can concurrently play {@linkplain Animation animations}.
 * <p>
 * Example:
 * <code> {@snippet lang = java:
 * class MyScreen extends Screen {
 *     // ctor, ...
 *
 *     protected void init() {
 *         IconWidget iconWidget = IconWidget.create(10, 10, ICON_TEXTURE);
 *         iconWidget.easeit$addAnimation(WidgetAnimation.ofInt(
 *             iconWidget,
 *             Easing.EXPONENTIAL,
 *             EasingType.OUT,
 *             20 * 5, // 5 seconds (or 100 ticks)
 *             Action.createResize(50, 50))
 *         );
 *     }
 * }} </code>
 * <p><br>
 * This gets injected automatically to {@link ClickableWidget ClickableWidgets} and {@link Screen Screens} using interface injection
 */
@ApiStatus.NonExtendable
public interface Animatable<T extends Animation<?>> {
    /**
     * Gets this {@link Widget}'s {@linkplain Animation animations}
     * @return a mutable {@link Deque} containing this {@link Widget}'s {@linkplain Animation animations}
     */
    default List<T> easeit$getAnimations() {
        throw new AssertionError("Implemented in Mixin");
    }

    /**
     * Adds multiple {@linkplain T animations} onto the {@linkplain #easeit$getAnimations() current animations} and plays them if possible
     * @param animations the {@linkplain T animations} to add
     * @return the added {@linkplain Animation animations}
     */
    default List<T> easeit$addAnimations(List<T> animations) {
        this.easeit$getAnimations().addAll(animations);
        return animations;
    }

    /**
     * Adds multiple {@linkplain Animation animations} onto the {@linkplain #easeit$getAnimations() current animations} and plays them if possible
     * @param animations the {@linkplain Animation animations} to add
     * @return the added {@linkplain Animation animations}
     */
    default List<T> easeit$addAnimations(T... animations) {
        List<T> asList = List.of(animations);

        this.easeit$getAnimations().addAll(asList);
        return asList;
    }

    /**
     * Adds an {@link WidgetAnimation} onto the {@linkplain #easeit$getAnimations() current animations} and plays it if possible
     * @param animation the {@link WidgetAnimation} to add
     * @return the added {@link WidgetAnimation}
     * @see #easeit$addAnimation(Animation, Class, Animation.AnimationTickCallback) 
     */
    default <WidgetT extends T> WidgetT easeit$addAnimation(WidgetT animation) {
        this.easeit$getAnimations().add(animation);
        return animation;
    }

    /**
     * Adds an {@link WidgetAnimation} onto the {@linkplain #easeit$getAnimations() current animations} and plays it if possible
     * @param animation the {@link WidgetAnimation} to add
     * @param numberType the type of the given animation
     * @param tickCallback the callback to call whenever the given {@link WidgetAnimation} ticks
     * @return the added {@link WidgetAnimation}
     * @see #easeit$addAnimation(Animation)
     */
    default <WidgetT extends T, N extends Number> WidgetT easeit$addAnimation(WidgetT animation, Class<N> numberType, Animation.AnimationTickCallback<N> tickCallback) {
        if (!numberType.equals(animation.getNumberType()))
            throw new IllegalArgumentException("Incompatible animation number type. Expected %s but got %s".formatted(animation.getNumberType(), numberType));

        // noinspection unchecked
        Animation<N> typedAnimation = (Animation<N>) animation;
        typedAnimation.setCallback(tickCallback);
        
        return this.easeit$addAnimation(animation);
    }


    /**
     * This is called every time the widget ticks thanks to mixins.
     * This is used to tick every {@linkplain WidgetAnimation animations}
     * @see WidgetAnimation#tick
     */
    default void easeit$tickWidgetAnimations() {
        List<T> animations = this.easeit$getAnimations();
        animations.forEach(Animation::tick);
        animations.stream()
                .filter(Animation::isFinished)
                .forEach(animation ->
                    this.easeit$getAnimations().remove(animation)
                );
    }
}
