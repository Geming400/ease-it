package fr.geming400.easeit.client.animation.easing;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

/**
 * An easing which uses float values instead of double values.
 * <p>
 * For example, this is useful for {@linkplain ClickableWidget clickable widgets} which uses a float value for its alpha channel
 * @see Easing
 * @see IntEasing
 * @see <a href="https://easing.net">easings.net</a>
 */
@ApiStatus.NonExtendable
public class FloatEasing extends WrappedEasing<Float, Double, Easing> {
    private FloatEasing(Easing easing) {
        super(easing);
    }

    @Range(from = 0, to = 100)
    @Override
    public Float in(@Range(from = 0, to = 1) double x) {
        return this.easing.in(x).floatValue();
    }
    @Override
    public Float out(@Range(from = 0, to = 1) double x) {
        return this.easing.in(x).floatValue();
    }
    @Range(from = 0, to = 100)
    @Override
    public Float inOut(@Range(from = 0, to = 1) double x) {
        return this.easing.in(x).floatValue();
    }

    @Override
    public Float in(@Range(from = 0, to = 1) double x, Float min, Float max) {
        return easing.in(x, min.doubleValue(), max.doubleValue()).floatValue();
    }
    @Override
    public Float out(@Range(from = 0, to = 1) double x, Float min, Float max) {
        return easing.out(x, min.doubleValue(), max.doubleValue()).floatValue();
    }
    @Override
    public Float inOut(@Range(from = 0, to = 1) double x, Float min, Float max) {
        return easing.inOut(x, min.doubleValue(), max.doubleValue()).floatValue();
    }

    @Override
    public Float in(@Range(from = 0, to = 1) double x, Float max) {
        return this.in(x, 0.0f, max);
    }
    @Override
    public Float out(@Range(from = 0, to = 1) double x, Float max) {
        return this.out(x, 0.0f, max);
    }
    @Override
    public Float inOut(@Range(from = 0, to = 1) double x, Float max) {
        return this.inOut(x, 0.0f, max);
    }


    @Override
    public Easing getWrappedEasing() {
        return easing;
    }

    @Override
    public Easing asDoubleEasing() {
        return this.getWrappedEasing();
    }

    @Override
    public IntEasing asIntEasing() {
        return IntEasing.of(this);
    }

    @Override
    public FloatEasing asFloatEasing() {
        return this;
    }

    /**
     * Creates an {@link FloatEasing} based of a {@link Easing} which uses {@code doubles}
     * @param easing the {@link Easing} to convert
     * @return the created {@link FloatEasing}
     */
     static FloatEasing of(Easing easing) {
        return new FloatEasing(easing);
    }

    /**
     * Creates an {@link FloatEasing} based of a {@link WrappedEasing} wrapped around a {@link Easing}
     * @param easing the {@link WrappedEasing} to convert
     * @return the created {@link FloatEasing}
     */
    static FloatEasing of(WrappedEasing<?, ?, Easing> easing) {
        return new FloatEasing(easing.getWrappedEasing());
    }
}
