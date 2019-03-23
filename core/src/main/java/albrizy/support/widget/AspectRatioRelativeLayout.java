package albrizy.support.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class AspectRatioRelativeLayout extends RelativeLayout {

    private final AspectRatioHelper helper;

    public AspectRatioRelativeLayout(Context context) {
        this(context, null);
    }

    public AspectRatioRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioRelativeLayout(Context context, @Nullable AttributeSet attrs, int defAttr) {
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
