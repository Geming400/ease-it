package fr.geming400.easeit.client.animation.easing;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Range;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface BaseEasing<T extends Number> {
    /**
     * Returns the "in" value of the "in" function
     * @param x a value describing the {@code x} coordinate ({@code easing.in(x)})
     * @return a value between {@code 0} and {@code 1}. It's the {@code y} coordinate ({@code y = easing.in(x)})
     * @see #inOut(double)
     */
    T in(@Range(from = 0, to = 1) double x);
    /**
     * Returns the "out" value of the "out" function
     * @param x a value describing the {@code x} coordinate ({@code easing.out(x)})
     * @return a value between {@code 0} and {@code 1}. It's the {@code y} coordinate ({@code y = easing.out(x)})
     * @see #inOut(double)
     */
    T out(@Range(from = 0, to = 1) double x);
    /**
     * Returns the "in out" value of the "in out" function. It's a mix between the {@link #in(double)} and {@link #out(double)} functions
     * @param x a value describing the {@code x} coordinate ({@code easing.inOut(x)})
     * @return a value between {@code 0} and {@code 1}. It's the {@code y} coordinate ({@code y = easing.inOut(x)})
     * @see #in(double)
     * @see #out(double)
     */
    T inOut(@Range(from = 0, to = 1) double x);

    /**
     * Returns the "in" value of the "in" function
     * @param x value describing the {@code x} coordinate ({@code easing.in(x)})
     * @param from the minimum value to return
     * @param to the maximum value to return
     * @return a value between {@code from} and {@code to}. It's the {@code y} coordinate ({@code y = easing.in(x)})
     */
    T in(@Range(from = 0, to = 1) double x, T from, T to);
    /**
     * Returns the "out" value of the "out" function
     * @param x value describing the {@code x} coordinate ({@code easing.out(x)})
     * @param from the minimum value to return
     * @param to the maximum value to return
     * @return a value between {@code from} and {@code to}. It's the {@code y} coordinate ({@code y = easing.out(x)})
     */
    T out(@Range(from = 0, to = 1) double x, T from, T to);
    /**
     * Returns the "in out" value of the "in out" function
     * @param x value describing the {@code x} coordinate ({@code easing.inOut(x)})
     * @param from the minimum value to return
     * @param to the maximum value to return
     * @return a value between {@code from} and {@code to}. It's the {@code y} coordinate ({@code y = easing.inOut(x)})
     */
    T inOut(@Range(from = 0, to = 1) double x, T from, T to);

    /**
     * Returns the "in" value of the "in" function
     * @param x value describing the {@code x} coordinate ({@code easing.in(x)})
     * @param to the maximum value to return
     * @return a value between {@code 0} and {@code to}. It's the {@code y} coordinate ({@code y = easing.in(x)})
     */
    T in(@Range(from = 0, to = 1) double x, T to);
    /**
     * Returns the "out" value of the "out" function
     * @param x value describing the {@code x} coordinate ({@code easing.out(x)})
     * @param to the maximum value to return
     * @return a value between {@code 0} and {@code to}. It's the {@code y} coordinate ({@code y = easing.out(x)})
     */
    T out(@Range(from = 0, to = 1) double x, T to);
    /**
     * Returns the "in out" value of the "in out" function
     * @param x value describing the {@code x} coordinate ({@code easing.inOut(x)})
     * @param to the maximum value to return
     * @return a value between {@code 0} and {@code to}. It's the {@code y} coordinate ({@code y = easing.inOut(x)})
     */
    T inOut(@Range(from = 0, to = 1) double x, T to);

    IntEasing asIntEasing();
    FloatEasing asFloatEasing();
    Easing asDoubleEasing();

    /**
     * Returns the {@link FullEasingFunction} according to the given {@link EasingType}
     * @param easingType the {@link EasingType}
     * @return the {@link FullEasingFunction}
     */
    default FullEasingFunction<T> getEasingFunc(EasingType easingType) {
        return FullEasingFunction.of(this, easingType);
    }

    /**
     * Normalizes the {@code x} coordinate of easing functions in the range {@code [0, 1]}
     * @param x the {@code x} coordinate
     * @return the normalized {@code x} coordinate
     */
    @Range(from = 0, to = 1)
    static double normalizeValue(double x) {
        return Math.clamp(x, 0, 1);
    }

    static void minMaxCheck(double from, double to) {
        if (from == to)
            throw new IllegalArgumentException("min and max value are equal");
    }

    @FunctionalInterface
    interface EasingFunction<T extends Number> {
        /**
         * Gets the {@code y} value of the function at the given progress
         * @param x a value describing the {@code x} coordinate (eg: {@code easing.in(x)})
         * @return the {@code y} value of the function at the given progress ({@code x})
         */
        T get(@Range(from = 0, to = 1) double x);
    }

    interface FullEasingFunction<T extends Number> extends EasingFunction<T> {
        /// Refer to {@linkplain #in(double, Number) this} for Javadoc
        T get(@Range(from = 0, to = 1) double x, T to);
        /// Refer to {@linkplain #in(double, Number, Number) this} for Javadoc
        T get(@Range(from = 0, to = 1) double x, T from, T to);

        static <T extends Number> FullEasingFunction<T> of(Function<Double, T> normalFunc, BiFunction<Double, T, T> maxFunc, TriFunction<Double, T, T, T> minMaxFunc) {
            return new FullEasingFunction<>() {
                @Override
                public T get(double x) {
                    return normalFunc.apply(x);
                }

                @Override
                public T get(double x, T to) {
                    return maxFunc.apply(x, to);
                }

                @Override
                public T get(double x, T from, T to) {
                    return minMaxFunc.apply(x, from, to);
                }
            };
        }

        static <T extends Number> FullEasingFunction<T> of(BaseEasing<T> easing, EasingType easingType) {
            return switch (easingType) {
                case OUT -> of(easing::out, easing::out, easing::out);
                case IN_OUT -> of(easing::inOut, easing::inOut, easing::inOut);
                default -> of(easing::in, easing::in, easing::in);
            };
        }
    }
}
