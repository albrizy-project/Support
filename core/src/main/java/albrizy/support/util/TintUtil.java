package albrizy.support.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import albrizy.support.theme.Theme;

public class TintUtil {

    private TintUtil() {}

    @Nullable
    public static Drawable tint(Context context, @DrawableRes int resId) {
        Drawable dr = ContextCompat.getDrawable(context, resId);
        return tint(dr, Theme.getIconColor(context));
    }

    @Nullable
    public static Drawable tint(Context context, @DrawableRes int resId, @ColorInt int color) {
        Drawable dr = ContextCompat.getDrawable(context, resId);
        return tint(dr, color);
    }

    @Nullable
    public static Drawable tint(Context context, @Nullable Drawable drawable) {
        return tint(drawable, Theme.getIconColor(context));
    }

    @Nullable
    public static Drawable tint(@Nullable Drawable drawable, @ColorInt int color) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            DrawableCompat.setTint(drawable, color);
            return drawable;
        }
        return null;
    }

    @Nullable
    public static Drawable tint(@Nullable Drawable drawable, @NonNull ColorStateList csl) {
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, csl);
            return drawable;
        }
        return null;
    }
}
