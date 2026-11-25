package fr.geming400.easeit;

import fr.geming400.easeit.client.animation.easing.*;
import net.minecraft.util.math.random.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EasingTests {
    private static final Random RANDOM = Random.create();

    @Test
    void resultGetTests() {
        int i = 0;
        for (Easing doubleEasing : Easing.BUILTIN_EASINGS) {
            FloatEasing floatEasing = doubleEasing.asFloatEasing();
            IntEasing intEasing = doubleEasing.asIntEasing();

            System.out.printf("Testing easing %s (i = %s)%n", doubleEasing, i);

            for (EasingType easingType : EasingType.values()) {
                BaseEasing.FullEasingFunction<Double> easingFunc = doubleEasing.getEasingFunc(easingType);
                CustomAssertions.assertApproximatelyEquals(0, easingFunc.get(0));
                CustomAssertions.assertApproximatelyEquals(1, easingFunc.get(1));
                CustomAssertions.assertNotEquals(1, easingFunc.get(getClampedRandomDouble()));
                CustomAssertions.assertNotEquals(0, easingFunc.get(getClampedRandomDouble()));

                CustomAssertions.assertEquals(0, easingFunc.get(0, 10.0));
                CustomAssertions.assertEquals(10, easingFunc.get(0, 10.0, 15.0));
                CustomAssertions.assertApproximatelyEquals(10, easingFunc.get(1, 10.0));
                CustomAssertions.assertApproximatelyEquals(15, easingFunc.get(1, 10.0, 15.0));
            }

            for (EasingType easingType : EasingType.values()) {
                BaseEasing.FullEasingFunction<Float> easingFunc = floatEasing.getEasingFunc(easingType);
                CustomAssertions.assertApproximatelyEquals(0, easingFunc.get(0));
                CustomAssertions.assertApproximatelyEquals(1, easingFunc.get(1));
                CustomAssertions.assertNotEquals(1, easingFunc.get(getClampedRandomDouble()));
                CustomAssertions.assertNotEquals(0, easingFunc.get(getClampedRandomDouble()));

                CustomAssertions.assertEquals(0, easingFunc.get(0, 10f));
                CustomAssertions.assertEquals(10, easingFunc.get(0, 10f, 15f));
                CustomAssertions.assertApproximatelyEquals(10, easingFunc.get(1, 10f));
                CustomAssertions.assertApproximatelyEquals(15, easingFunc.get(1, 10f, 15f));
            }

            for (EasingType easingType : EasingType.values()) {
                BaseEasing.FullEasingFunction<Integer> easingFunc = intEasing.getEasingFunc(easingType);
                Assertions.assertEquals(0, easingFunc.get(0));
                Assertions.assertEquals(100, easingFunc.get(1));

                Assertions.assertEquals(0, easingFunc.get(0, 10));
                Assertions.assertEquals(10, easingFunc.get(0, 10, 15));
                Assertions.assertEquals(10, easingFunc.get(1, 10));
                Assertions.assertEquals(15, easingFunc.get(1, 10, 15));
            }

            i++;
        }
    }

    private static double getClampedRandomDouble() {
        return Math.clamp(RANDOM.nextDouble(), 0.1, 0.2);
    }

    private static double getClampedRandomFloat() {
        return Math.clamp(RANDOM.nextFloat(), 0.1, 0.2);
    }
}
