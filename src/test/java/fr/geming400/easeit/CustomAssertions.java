package fr.geming400.easeit;

import net.minecraft.util.math.MathHelper;
import org.opentest4j.AssertionFailedError;

public final class CustomAssertions {
    private CustomAssertions() {}

    public static void assertApproximatelyEquals(float expected, float actual) {
        if (!MathHelper.approximatelyEquals(expected, actual))
            throw new AssertionFailedError("Expected <%s> but was: <%s>".formatted(expected, actual));
    }

    public static void assertApproximatelyEquals(double expected, double actual) {
        if (!MathHelper.approximatelyEquals(expected, actual))
            throw new AssertionFailedError("Expected <%s> but was: <%s>".formatted(expected, actual));
    }

    // JUnit fails when comparing -0.0 == 0.0 (it compares the bits instead of using the comparison operator)
    public static void assertNotEquals(double expected, double actual) {
        if (expected == actual)
            throw new AssertionFailedError("expected: not equal but was: <%s> but got <%s> instead".formatted(actual, expected));
    }

    // JUnit fails when comparing -0.0 == 0.0 (it compares the bits instead of using the comparison operator)
    public static void assertEquals(double expected, double actual) {
        if (expected != actual)
            throw new AssertionFailedError("Expected <%s> but was: <%s>".formatted(expected, actual));
    }
}
