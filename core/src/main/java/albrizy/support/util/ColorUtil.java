package albrizy.support.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;

@SuppressWarnings("WeakerAccess")
public class ColorUtil {

    private ColorUtil() {}

    @ColorInt
    public static int stripAlpha(@ColorInt int color) {
        return  Color.rgb(Color.red(color),
                Color.green(color),
                Color.blue(color));
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color) {
        return adjustAlpha(color, 0.5f);
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, @FloatRange(from = 0.0, to = 1.0) float f) {
        int alpha = Math.round(Color.alpha(color) * f);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @ColorInt
    public static int invertColor(@ColorInt int color) {
        int red = 255 - Color.red(color);
        int green = 255 - Color.green(color);
        int blue = 255 - Color.blue(color);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    @ColorInt
    public static int blendColors(@ColorInt int startColor, @ColorInt int endColor, float ratio) {
        float inverseRatio = 1f - ratio;
        float alpha = (Color.alpha(startColor) * inverseRatio) + (Color.alpha(endColor) * ratio);
        float red = (Color.red(startColor)   * inverseRatio) + (Color.red(endColor)   * ratio);
        float green = (Color.green(startColor) * inverseRatio) + (Color.green(endColor) * ratio);
        float blue = (Color.blue(startColor)  * inverseRatio) + (Color.blue(endColor)  * ratio);
        return Color.argb((int) alpha, (int) red, (int) green, (int) blue);
    }

    @ColorInt
    public static int lightenColor(@ColorInt int color) {
        return shiftColor(color, 1.1f);
    }

    @ColorInt
    public static int darkenColor(@ColorInt int color) {
        return shiftColor(color, 0.9f);
    }

    @ColorInt
    public static int darkenColor(@ColorInt int color, float f) {
        return shiftColor(color, f);
    }

    @ColorInt
    public static int shiftColor(@ColorInt int color, @FloatRange(from = 0.0f, to = 2.0f) float f) {
        if (f == 1f) return color;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= f;
        return Color.HSVToColor(hsv);
    }

    public static boolean isColorLight(@ColorInt int color) {
        if (color == Color.BLACK) return false;
        else if (color == Color.WHITE || color == Color.TRANSPARENT) return true;
        final double darkness = 1    - (0.299
                * Color.red(color)   + 0.587
                * Color.green(color) + 0.114
                * Color.blue(color)) / 255;
        return darkness < 0.4;
    }
}
