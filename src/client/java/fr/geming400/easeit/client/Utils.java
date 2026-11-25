package fr.geming400.easeit.client;

import net.minecraft.util.math.ColorHelper;

public final class Utils {
    private Utils() {}

    /**
     * Sets the given alpha to the argb value.
     * When rendering text, if the alpha value is {@code < 0x04} then it renders
     * as it the alpha value was {@code 0xFF} instead
     * @see ColorHelper.Argb#withAlpha
     */
    public static int withFixedAlpha(int alpha, int argb) {
        return alpha < 0x05
                ? ColorHelper.Argb.withAlpha(0x05, argb)
                : ColorHelper.Argb.withAlpha(alpha, argb);
    }

    public static int withFixedAlpha(float alpha, int argb) {
        return withFixedAlpha((int) (alpha * 255), argb);
    }
}
