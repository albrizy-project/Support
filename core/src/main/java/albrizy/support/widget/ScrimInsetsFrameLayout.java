package albrizy.support.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

public class ScrimInsetsFrameLayout extends FrameLayout {

    public ScrimInsetsFrameLayout(Context context) {
        super(context);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrimInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return ScrimInsetsFrameLayout.onApplyWindowInsets(this, insets);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    static WindowInsets onApplyWindowInsets(final ViewGroup group, final WindowInsets insets) {
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).dispatchApplyWindowInsets(insets);
        }
        return insets;
    }
}
