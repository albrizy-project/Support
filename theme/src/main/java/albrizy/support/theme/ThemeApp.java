package albrizy.support.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import albrizy.support.theme.color.ColorAccent;
import albrizy.support.theme.color.ColorBackground;
import albrizy.support.theme.color.ColorPrimary;

public class ThemeApp {

    public static ThemeApp with(Context context) {
        return new ThemeApp(context);
    }

    private final Editor editor;

    @SuppressLint("CommitPrefEdits")
    private ThemeApp(Context context) {
        editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
    }

    public ThemeApp colorPrimary(ColorPrimary primary) {
        editor.putString("colorPrimary", primary.name());
        return this;
    }

    public ThemeApp colorAccent(ColorAccent accent) {
        editor.putString("colorAccent", accent.name());
        return this;
    }

    public ThemeApp colorBackground(ColorBackground background) {
        editor.putString("colorBackground", background.name());
        return this;
    }

    public void apply() {
        editor.apply();
    }
}
