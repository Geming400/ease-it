package fr.geming400.easeit.client.animation.animatable;

import fr.geming400.easeit.client.animation.Animation;
import fr.geming400.easeit.client.animation.WidgetAnimation;
import net.minecraft.client.gui.widget.Widget;

import java.util.Deque;
import java.util.List;

public interface BaseAnimatable<T extends Animation<?>> {
    /**
     * Gets this {@link Widget}'s {@linkplain Animation animations}
     * @return a mutable {@link Deque} containing this {@link Widget}'s {@linkplain Animation animations}
     */
    default List<T> easeit$getAnimations() {
        throw new AssertionError("Implemented in Mixin");
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
