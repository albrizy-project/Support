package albrizy.support.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

public class ScrimInsetsRelativeLayout extends RelativeLayout {

    public ScrimInsetsRelativeLayout(Context context) {
        super(context);
    }

    public ScrimInsetsRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrimInsetsRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return ScrimInsetsFrameLayout.onApplyWindowInsets(this, insets);
    }
}
