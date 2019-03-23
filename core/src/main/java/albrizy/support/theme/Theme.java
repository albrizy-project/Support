package albrizy.support.theme;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import albrizy.support.R;
import albrizy.support.theme.customizer.SystemBarCustomizer;
import albrizy.support.util.ColorUtil;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;

@SuppressWarnings("WeakerAccess")
public class Theme {

    private Theme() {}

    public static void theme(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            Window window = activity.getWindow();
            if (activity instanceof SystemBarCustomizer) {
                SystemBarCustomizer customizer = (SystemBarCustomizer) activity;
                window.setStatusBarColor(customizer.getStatusBarColor());
                window.setNavigationBarColor(customizer.getNavigationBarColor());
            } else {
                window.setStatusBarColor(getStatusBarColor(activity));
                window.setNavigationBarColor(getPrimaryColor(activity));
            }
        }
    }

    public static void setNavigationBarColor(AppCompatActivity activity) {
        setNavigationBarColor(activity, getPrimaryColor(activity));
    }

    public static void setNavigationBarColor(AppCompatActivity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }
    }

    public static void setStatusBarColor(AppCompatActivity activity) {
        setStatusBarColor(activity, getStatusBarColor(activity));
    }

    public static void setStatusBarColor(AppCompatActivity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    public static void setStatusBarLight(View view) {
        setStatusBarLight(view, true);
    }

    public static void setStatusBarLight(View view, boolean light) {
        if (Build.VERSION.SDK_INT >= M) {
            final int currentVisibility = view.getSystemUiVisibility();
            view.setSystemUiVisibility(light
                    ? (currentVisibility |  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    : (currentVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            );
        }
    }

    @ColorInt
    public static int getStatusBarColor(Context context) {
        int color = getPrimaryColor(context);
        return ColorUtil.darkenColor(color);
    }

    @ColorInt
    public static int getPrimaryColor(Context context) {
        return resolveColor(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryDarkColor(Context context) {
        return resolveColor(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getAccentColor(Context context) {
        return resolveColor(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getIconColor(Context context) {
        return resolveColor(context, R.attr.colorControlNormal);
    }

    @ColorInt
    public static int resolveColor(Context context, @AttrRes int attr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
}
