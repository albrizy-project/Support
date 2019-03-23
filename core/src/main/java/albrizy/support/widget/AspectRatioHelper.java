package albrizy.support.widget;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

import albrizy.support.R;

import static android.view.View.MeasureSpec.EXACTLY;

class AspectRatioHelper {

    private final View view;
    private int widthRatio;
    private int heightRatio;

    AspectRatioHelper(View view) {
        this.view = view;
    }

    void init(AttributeSet attrs) {
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.AspectRatioView);
        widthRatio = a.getInteger(R.styleable.AspectRatioView_widthRatio, 1);
        heightRatio = a.getInteger(R.styleable.AspectRatioView_heightRatio, 1);
        a.recycle();
    }

    void setRatio(int widthRatio, int heightRatio) {
        if (this.widthRatio != widthRatio && this.heightRatio != heightRatio) {
            this.widthRatio  = widthRatio;
            this.heightRatio = heightRatio;
            view.requestLayout();
        }
    }

    @SuppressWarnings("UnusedAssignment")
    void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

        widthMeasureSpec  = MeasureSpec.makeMeasureSpec(widthSize, EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, EXACTLY);
    }
}
