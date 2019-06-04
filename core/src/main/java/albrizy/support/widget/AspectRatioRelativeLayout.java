package albrizy.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import albrizy.support.R;

import static android.view.View.MeasureSpec.EXACTLY;

public class AspectRatioRelativeLayout extends RelativeLayout {

    private int widthRatio;
    private int heightRatio;

    public AspectRatioRelativeLayout(Context context) {
        this(context, null);
    }

    public AspectRatioRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public AspectRatioRelativeLayout(Context context, @Nullable AttributeSet attrs, int defAttr) {
        super(context, attrs, defAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioView);
            widthRatio = a.getInteger(R.styleable.AspectRatioView_widthRatio, 1);
            heightRatio = a.getInteger(R.styleable.AspectRatioView_heightRatio, 1);
            a.recycle();
        }
    }

    public void setRatio(int widthRatio, int heightRatio) {
        if (this.widthRatio != widthRatio && this.heightRatio != heightRatio) {
            this.widthRatio  = widthRatio;
            this.heightRatio = heightRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == EXACTLY) {
            if (heightMode != EXACTLY) {
                heightSize = (int) (widthSize * 1f / widthRatio * heightRatio);
            }
        } else if (heightMode == EXACTLY) {
            widthSize = (int) (heightSize * 1f / heightRatio * widthRatio);
        } else {
            throw new IllegalStateException("Either width or height must be EXACTLY.");
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
