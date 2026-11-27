package fr.geming400.easeit.client.animation.animatable;

import fr.geming400.easeit.client.animation.Animation;
import fr.geming400.easeit.client.animation.HudAnimation;
import fr.geming400.easeit.client.animation.WidgetAnimation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * An {@link HudAnimatable} can be used to animated anything that isn't a {@link ClickableWidget Widget}.
 * To animate a  use {@link #easeit$addAnimation(HudAnimation)}
 * <p>
 * {@linkplain HudAnimatable Animatable}s can concurrently play {@linkplain HudAnimatable animations}.
 * <p>
 * Example:
 * <code> {@snippet lang = java:
 * 		GlobalAnimatable.getHud().easeit$addAnimation(HudAnimation.ofInt(
 * 				Easing.EXPONENTIAL, EasingType.OUT,
 * 				20 * 5,
 * 				(animation, progress, easingFunction) -> {
 * 					// You can run some of the stuff from the built-in actions
 * 					// using the ActionHelpers class
 * 				}
 * 		));
 * } </code>
 * <p><br>
 * To use it, you can use the {@link GlobalAnimatable} class.
 */
@ApiStatus.NonExtendable
public interface HudAnimatable extends BaseAnimatable<HudAnimation<?>> {
    /**
     * Adds an {@link HudAnimation} onto the {@linkplain #easeit$getAnimations() current animations} and plays it
     * @param animation the {@link HudAnimation} to add
     * @return the added {@link HudAnimation}
     */
    default <T extends Number> HudAnimation<T> easeit$addAnimation(HudAnimation<T> animation) {
        this.easeit$getAnimations().add(animation);
        return animation;
    }


    /**
     * This is called every time the widget ticks thanks to mixins.
     * This is used to tick every {@linkplain WidgetAnimation animations}
     * @param drawContext the draw context used for the rendering the of animations
     * @see WidgetAnimation#tick
     */
    default void easeit$tickWidgetAnimations(DrawContext drawContext) {
        List<HudAnimation<?>> animations = this.easeit$getAnimations();
        animations.forEach(animation -> animation.tick(drawContext));
        animations.stream()
                .filter(Animation::isFinished)
                .forEach(animation ->
                        this.easeit$getAnimations().remove(animation)
                );
    }
}
