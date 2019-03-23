package albrizy.support.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.LinearLayout;

public class ScrimInsetsLinearLayout extends LinearLayout {

    public ScrimInsetsLinearLayout(Context context) {
        super(context);
    }

    public ScrimInsetsLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrimInsetsLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return ScrimInsetsFrameLayout.onApplyWindowInsets(this, insets);
    }
}
