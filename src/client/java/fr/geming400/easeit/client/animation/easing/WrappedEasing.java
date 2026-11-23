package fr.geming400.easeit.client.animation.easing;

/**
 * A wrapper for {@link Easing}s
 * @param <T> The easing number type (ex: {@link IntEasing} is {@link Integer})
 * @param <N> The number type of the wrapped easing
 * @param <E> The wrapped easing
 * @see IntEasing
 * @see FloatEasing
 */
public abstract class WrappedEasing<T extends Number, N extends Number, E extends BaseEasing<N>> implements BaseEasing<T> {
    protected final E easing;

    protected WrappedEasing(E easing) {
        this.easing = easing;
    }

    public E getWrappedEasing() {
        return this.easing;
    }
}
