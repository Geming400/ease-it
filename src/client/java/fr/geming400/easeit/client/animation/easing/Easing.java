package fr.geming400.easeit.client.animation.easing;

import com.mojang.datafixers.util.Function4;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

/**
 * Contains different easing functions, this is used for smooth movements.
 * <p>
 * To visualize how each function look like, refer to <a href="https://easing.net">easings.net</a>.
 * @see IntEasing
 * @see FloatEasing
 * @see <a href="https://easing.net">easings.net</a>
 */
@ApiStatus.NonExtendable
public interface Easing extends BaseEasing<Double> {
    Easing LINEAR = of(x -> x);
    Easing SINE = of(
            x -> 1 - Math.cos((x * Math.PI) / 2),
            x -> Math.sin((x * Math.PI) / 2),
            x -> -(Math.cos(Math.PI * x) - 1) / 2
    );
    Easing CUBIC = of(
            x -> x * x * x,
            x -> 1 - Math.pow(1 - x, 3),
            x -> x < 0.5
                    ? 4 * x * x * x
                    : 1 - Math.pow(-2 * x + 2, 3) / 2
    );
    Easing QUINT = of(
            x -> x * x * x * x,
            x -> 1 - Math.pow(1 - x, 5),
            x -> x < 0.5
                    ? 16 * x * x * x * x * x
                    : 1 - Math.pow(-2 * x + 2, 5) / 2
    );
    Easing CIRCLE = of(
            x -> 1 - Math.sqrt(1 - Math.pow(x, 2)),
            x -> Math.sqrt(1 - Math.pow(x - 1, 2)),
            x -> x < 0.5
                    ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
                    : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2
    );
// It's values are not bound between [0, 1], it's commented out to prevent any issues
//    Easing ELASTIC = of(
//            x -> {
//                double c4 = (2 * Math.PI) / 3;
//
//                return x == 0
//                        ? 0
//                        : x == 1
//                        ? 1
//                        : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4);
//            },
//            x -> {
//                double c4 = (2 * Math.PI) / 3;
//
//                return x == 0
//                        ? 0
//                        : x == 1
//                        ? 1
//                        : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
//            },
//            x -> {
//                double c5 = (2 * Math.PI) / 4.5;
//                double sinThingy = Math.sin((20 * x - 11.125) * c5);
//
//                return x == 0
//                        ? 0
//                        : x == 1
//                        ? 1
//                        : x < 0.5
//                        ? -(Math.pow(2, 20 * x - 10) * sinThingy) / 2
//                        : (Math.pow(2, -20 * x + 10) * sinThingy) / 2 + 1;
//            }
//    );
    Easing QUADRATIC = of(
            x -> x * x,
            x -> 1 - (1 - x) * (1 - x),
            x -> x < 0.5
                    ? 2 * x * x
                    : 1 - Math.pow(-2 * x + 2, 2) / 2
    );
    Easing QUARTIC = of(
            x -> x * x * x * x,
            x -> 1 - Math.pow(1 - x, 4),
            x -> x < 0.5
                    ? 8 * x * x * x * x
                    : 1 - Math.pow(-2 * x + 2, 4) / 2
    );
    Easing EXPONENTIAL = of(
            x -> x == 0
                    ? 0
                    : Math.pow(2, 10 * x - 10),
            x -> x == 1
                    ? 1
                    : 1 - Math.pow(2, -10 * x),
            x -> x == 0
                    ? 0
                    : x == 1
                    ? 1
                    : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                    : (2 - Math.pow(2, -20 * x + 10)) / 2
    );
// It's values are not bound between [0, 1], it's commented out to prevent any issues
//    Easing BACK = of(
//            x -> {
//                double c1 = 1.70158;
//                double c3 = c1 + 1;
//
//                return c3 * x * x * x - c1 * x * x;
//            },
//            x -> {
//                double c1 = 1.70158;
//                double c3 = c1 + 1;
//
//                return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
//            },
//            x -> {
//                double c1 = 1.70158;
//                double c2 = c1 * 1.525;
//
//                return x < 0.5
//                        ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
//                        : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2;
//            }
//    );
    Easing BOUNCE = withFunctionsAccess(
            (in, out, inOut, x) -> 1 - out.get(1 - x),
            (in, out, inOut, x) -> {
                double n1 = 7.5625;
                double d1 = 2.75;

                if (x < 1 / d1) {
                    return n1 * x * x;
                } else if (x < 2 / d1) {
                    return n1 * (x -= 1.5 / d1) * x + 0.75;
                } else if (x < 2.5 / d1) {
                    return n1 * (x -= 2.25 / d1) * x + 0.9375;
                } else {
                    return n1 * (x -= 2.625 / d1) * x + 0.984375;
                }
            },
            (in, out, inOut, x) -> x < 0.5
                    ? (1 - out.get(1 - 2 * x)) / 2
                    : (1 + out.get(2 * x - 1)) / 2
    );


    /**
     * {@inheritDoc}
     * @param x a value between {@code 0} and {@code 1}. It's the {@code x} coordinate ({@code easing.in(x)})
     */
    @Override
    Double in(@Range(from = 0, to = 1) double x);
    /**
     * {@inheritDoc}
     * @param x a value between {@code 0} and {@code 1}. It's the {@code x} coordinate ({@code easing.out(x)})
     */
    @Override
    Double out(@Range(from = 0, to = 1) double x);
    /**
     * {@inheritDoc}
     * @param x a value between {@code 0} and {@code 1}. It's the {@code x} coordinate ({@code easing.inOut(x)})
     */
    @Override
    Double inOut(@Range(from = 0, to = 1) double x);

    @Override
    default Double in(@Range(from = 0, to = 1) double x, Double min, Double max) {
        if (min == 0)
            return this.in(x) * Math.max(min, max);

        return this.in(x) * Math.max(min, max) + Math.min(min, max);
    }
    @Override
    default Double out(@Range(from = 0, to = 1) double x, Double min, Double max) {
        if (min == 0)
            return this.in(x) * Math.max(min, max);

        return this.out(x) * Math.max(min, max) + Math.min(min, max);
    }
    @Override
    default Double inOut(@Range(from = 0, to = 1) double x, Double min, Double max) {
        if (min == 0)
            return this.in(x) * Math.max(min, max);

        return this.inOut(x) * Math.max(min, max) + Math.min(min, max);
    }

    @Override
    default Double in(@Range(from = 0, to = 1) double x, Double max) {
        return this.in(x, 0.0, max);
    }
    @Override
    default Double out(@Range(from = 0, to = 1) double x, Double max) {
        return this.out(x, 0.0, max);
    }
    @Override
    default Double inOut(@Range(from = 0, to = 1) double x, Double max) {
        return this.inOut(x, 0.0, max);
    }

    default IntEasing asIntEasing() {
        return IntEasing.of(this);
    }

    @Override
    default Easing asDoubleEasing() {
        return this;
    }

    @Override
    default FloatEasing asFloatEasing() {
        return FloatEasing.of(this);
    }

    private static Easing of(EasingFunction<Double> in, EasingFunction<Double> out, EasingFunction<Double> inOut) {
        return new Easing() {
            @Override
            public Double in(double x) {
                return in.get(BaseEasing.normalizeValue(x));
            }

            @Override
            public Double out(double x) {
                return out.get(BaseEasing.normalizeValue(x));
            }

            @Override
            public Double inOut(double x) {
                return inOut.get(BaseEasing.normalizeValue(x));
            }
        };
    }

    private static Easing of(EasingFunction<Double> function) {
        return of(function, function ,function);
    }

    private static Easing withFunctionsAccess(
            //        in                      out                     inOut                   x       res
            Function4<EasingFunction<Double>, EasingFunction<Double>, EasingFunction<Double>, Double, Double> in,
            Function4<EasingFunction<Double>, EasingFunction<Double>, EasingFunction<Double>, Double, Double> out,
            Function4<EasingFunction<Double>, EasingFunction<Double>, EasingFunction<Double>, Double, Double> inOut
    ) {
        return new Easing() {
            @Override
            public Double in(double x) {
                return in.apply(this::in, this::out, this::inOut, BaseEasing.normalizeValue(x));
            }

            @Override
            public Double out(double x) {
                return out.apply(this::in, this::out, this::inOut, BaseEasing.normalizeValue(x));
            }

            @Override
            public Double inOut(double x) {
                return inOut.apply(this::in, this::out, this::inOut, BaseEasing.normalizeValue(x));
            }
        };
    }
}
