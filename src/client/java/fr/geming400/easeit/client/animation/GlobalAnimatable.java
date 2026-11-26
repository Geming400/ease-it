package fr.geming400.easeit.client.animation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A singleton to run {@linkplain SimpleAnimation animations} without being
 * limited by a {@linkplain net.minecraft.client.gui.widget.ClickableWidget widget} or a {@linkplain net.minecraft.client.gui.screen.Screen screen}
 */
public final class GlobalAnimatable implements SimpleAnimatable {
    private static GlobalAnimatable instance = null;

    private GlobalAnimatable() {}

    public static GlobalAnimatable getInstance() {
        if (instance == null)
            instance = new GlobalAnimatable();

        return instance;
    }

    private final List<SimpleAnimation<?>> playingAnimations = new CopyOnWriteArrayList<>();

    @Override
    public List<SimpleAnimation<?>> easeit$getAnimations() {
        return this.playingAnimations;
    }

    public static <T extends Number> SimpleAnimation<T> addAnimation(SimpleAnimation<T> animation) {
        return GlobalAnimatable.getInstance().easeit$addAnimation(animation);
    }

    public static List<SimpleAnimation<?>> getAnimations() {
        return GlobalAnimatable.getInstance().easeit$getAnimations();
    }
}
