package fr.geming400.easeit.client.animation.animatable;

import fr.geming400.easeit.client.animation.HudAnimation;
import fr.geming400.easeit.client.animation.SimpleAnimation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A singleton to run {@linkplain SimpleAnimation animations} without being
 * limited by a {@linkplain net.minecraft.client.gui.widget.ClickableWidget widget} or a {@linkplain net.minecraft.client.gui.screen.Screen screen}
 */
public final class GlobalAnimatable {
    private static SimpleGlobalAnimatable simpleInstance = null;
    private static HudGlobalAnimatable hudInstance = null;

    private GlobalAnimatable() {}

    /**
     * Gets the global {@link SimpleAnimatable} instance for {@linkplain SimpleAnimation simple animations}
     * @return the global {@link SimpleAnimatable} instance
     * @see SimpleAnimation
     */
    public static SimpleAnimatable getSimple() {
        if (simpleInstance == null)
            simpleInstance = new SimpleGlobalAnimatable();

        return simpleInstance;
    }

    /**
     * Gets the global {@link HudAnimatable} instance for {@linkplain HudAnimation HUD animations}
     * @return the global {@link HudAnimatable} instance
     * @see HudAnimation
     */
    public static HudAnimatable getHud() {
        if (hudInstance == null)
            hudInstance = new HudGlobalAnimatable();

        return hudInstance;
    }

    private static final class SimpleGlobalAnimatable implements SimpleAnimatable {
        private final List<SimpleAnimation<?>> playingAnimations = new CopyOnWriteArrayList<>();

        @Override
        public List<SimpleAnimation<?>> easeit$getAnimations() {
            return this.playingAnimations;
        }
    }

    private static final class HudGlobalAnimatable implements HudAnimatable {
        private final List<HudAnimation<?>> playingAnimations = new CopyOnWriteArrayList<>();

        @Override
        public List<HudAnimation<?>> easeit$getAnimations() {
            return this.playingAnimations;
        }
    }
}
