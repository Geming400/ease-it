package fr.geming400.easeit.client.animation.animatable;

import fr.geming400.easeit.client.animation.Action;
import fr.geming400.easeit.client.animation.Animation;
import fr.geming400.easeit.client.animation.WidgetAnimation;
import fr.geming400.easeit.client.animation.easing.Easing;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import org.jetbrains.annotations.ApiStatus;

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
public interface Animatable<T extends Animation<?>> extends BaseAnimatable<T> {
    /**
     * Adds multiple {@linkplain T animations} onto the {@linkplain #easeit$getAnimations() current animations} and plays them
     * @param animations the {@linkplain T animations} to add
     * @return the added {@linkplain Animation animations}
     */
    default List<T> easeit$addAnimations(List<T> animations) {
        this.easeit$getAnimations().addAll(animations);
        return animations;
    }

    /**
     * Adds multiple {@linkplain Animation animations} onto the {@linkplain #easeit$getAnimations() current animations} and plays them
     * @param animations the {@linkplain Animation animations} to add
     * @return the added {@linkplain Animation animations}
     */
    default List<T> easeit$addAnimations(T... animations) {
        List<T> asList = List.of(animations);

        this.easeit$getAnimations().addAll(asList);
        return asList;
    }

    /**
     * Adds an {@link WidgetAnimation} onto the {@linkplain #easeit$getAnimations() current animations} and plays it
     * @param animation the {@link WidgetAnimation} to add
     * @return the added {@link WidgetAnimation}
     */
    default <WidgetT extends T> WidgetT easeit$addAnimation(WidgetT animation) {
        this.easeit$getAnimations().add(animation);
        return animation;
    }
}
