package fr.geming400.easeit.client.animation.animatable;

import fr.geming400.easeit.client.animation.SimpleAnimation;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link SimpleAnimatable} can be used to animated anything that isn't a {@link ClickableWidget Widget}.
 * To animate a  use {@link #easeit$addAnimation(SimpleAnimation)}
 * <p>
 * {@linkplain SimpleAnimatable Animatable}s can concurrently play {@linkplain SimpleAnimatable animations}.
 * <p>
 * Example:
 * <code> {@snippet lang = java:
 * 		GlobalAnimatable.getSimple().easeit$addAnimation(SimpleAnimation.ofInt(
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
public interface SimpleAnimatable extends BaseAnimatable<SimpleAnimation<?>> {
    /**
     * Adds an {@link SimpleAnimation} onto the {@linkplain #easeit$getAnimations() current animations} and plays it
     * @param animation the {@link SimpleAnimation} to add
     * @return the added {@link SimpleAnimation}
     */
    default <T extends Number> SimpleAnimation<T> easeit$addAnimation(SimpleAnimation<T> animation) {
        this.easeit$getAnimations().add(animation);
        return animation;
    }
}
