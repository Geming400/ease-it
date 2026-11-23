package fr.geming400.easeit.client.animation.easing;

import net.minecraft.client.gui.widget.Widget;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

/**
 * An easing which uses integer values instead of double values.
 * <p>
 * For example, this is useful for minecraft {@linkplain Widget widgets} which uses integer positions
 * @see Easing
 * @see FloatEasing
 * @see <a href="https://easing.net">easings.net</a>
 */
@ApiStatus.NonExtendable
public class IntEasing extends WrappedEasing<Integer, Double, Easing> {
    private IntEasing(Easing easing) {
        super(easing);
    }

    /**
     * {@inheritDoc}
     * @return a value between {@code 0} and {@code 100}. It's the {@code y} coordinate ({@code y = easing.in(x)})
     */
    @Range(from = 0, to = 100)
    @Override
    public Integer in(@Range(from = 0, to = 1) double x) {
        return this.in(x, 100);
    }
    /**
     * {@inheritDoc}
     * @return a value between {@code 0} and {@code 100}. It's the {@code y} coordinate ({@code y = easing.out(x)})
     */
    @Range(from = 0, to = 100)
    @Override
    public Integer out(@Range(from = 0, to = 1) double x) {
        return this.out(x, 100);
    }
    /**
     * {@inheritDoc}
     * @return a value between {@code 0} and {@code 100}. It's the {@code y} coordinate ({@code y = easing.inOut(x)})
     */
    @Range(from = 0, to = 100)
    @Override
    public Integer inOut(@Range(from = 0, to = 1) double x) {
        return this.inOut(x, 100);
    }

    @Override
    public Integer in(@Range(from = 0, to = 1) double x, Integer min, Integer max) {
        return easing.in(x, min.doubleValue(), max.doubleValue()).intValue();
    }
    @Override
    public Integer out(@Range(from = 0, to = 1) double x, Integer min, Integer max) {
        return easing.out(x, min.doubleValue(), max.doubleValue()).intValue();
    }
    @Override
    public Integer inOut(@Range(from = 0, to = 1) double x, Integer min, Integer max) {
        return easing.inOut(x, min.doubleValue(), max.doubleValue()).intValue();
    }

    @Override
    public Integer in(@Range(from = 0, to = 1) double x, Integer max) {
        return this.in(x, 0, max);
    }
    @Override
    public Integer out(@Range(from = 0, to = 1) double x, Integer max) {
        return this.out(x, 0, max);
    }
    @Override
    public Integer inOut(@Range(from = 0, to = 1) double x, Integer max) {
        return this.inOut(x, 0, max);
    }


    @Override
    public Easing asDoubleEasing() {
        return this.getWrappedEasing();
    }

    @Override
    public IntEasing asIntEasing() {
        return this;
    }

    @Override
    public FloatEasing asFloatEasing() {
        return FloatEasing.of(this);
    }

    /**
     * Creates an {@link IntEasing} based of a {@link Easing} which uses {@code doubles}
     * @param easing the {@link Easing} to convert
     * @return the created {@link IntEasing}
     */
     static IntEasing of(Easing easing) {
        return new IntEasing(easing);
     }

    /**
     * Creates an {@link IntEasing} based of a {@link WrappedEasing} wrapped around a {@link Easing}
     * @param easing the {@link WrappedEasing} to convert
     * @return the created {@link IntEasing}
     */
    static IntEasing of(WrappedEasing<?, ?, Easing> easing) {
        return new IntEasing(easing.getWrappedEasing());
    }
}
