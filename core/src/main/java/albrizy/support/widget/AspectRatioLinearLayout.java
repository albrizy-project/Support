package albrizy.support.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class AspectRatioLinearLayout extends LinearLayout {

    private final AspectRatioHelper helper;

    public AspectRatioLinearLayout(Context context) {
        this(context, null);
    }

    public AspectRatioLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioLinearLayout(Context context, @Nullable AttributeSet attrs, int defAttr) {
        super(context, attrs, defAttr);
        helper = new AspectRatioHelper(this);
        helper.init(attrs);
    }

    public void setRatio(int widthRatio, int heightRatio) {
        helper.setRatio(widthRatio, heightRatio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        helper.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
