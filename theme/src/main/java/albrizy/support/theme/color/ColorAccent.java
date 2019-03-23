package albrizy.support.theme.color;

import android.support.annotation.StyleRes;

import albrizy.support.theme.R;

public enum ColorAccent {

    RED_A100(R.style.ThemeCompat_ColorAccent_RedA100),
    RED_A200(R.style.ThemeCompat_ColorAccent_RedA200),
    RED_A400(R.style.ThemeCompat_ColorAccent_RedA400),
    RED_A700(R.style.ThemeCompat_ColorAccent_RedA700),

    PINK_A100(R.style.ThemeCompat_ColorAccent_PinkA100),
    PINK_A200(R.style.ThemeCompat_ColorAccent_PinkA200),
    PINK_A400(R.style.ThemeCompat_ColorAccent_PinkA400),
    PINK_A700(R.style.ThemeCompat_ColorAccent_PinkA700),

    PURPLE_A100(R.style.ThemeCompat_ColorAccent_PurpleA100),
    PURPLE_A200(R.style.ThemeCompat_ColorAccent_PurpleA200),
    PURPLE_A400(R.style.ThemeCompat_ColorAccent_PurpleA400),
    PURPLE_A700(R.style.ThemeCompat_ColorAccent_PurpleA700);

    ColorAccent(@StyleRes int theme) {

    }
}
