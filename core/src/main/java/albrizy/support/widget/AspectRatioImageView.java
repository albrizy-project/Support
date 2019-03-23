package albrizy.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import albrizy.support.R;

@SuppressLint("AppCompatCustomView,CustomViewStyleable")
public class AspectRatioImageView extends ImageView {

    private static final int ORIENTATION_HORIZONTAL = 0;
    private static final int ORIENTATION_VERTICAL = 1;

    private int orientation;
    private float ratio;

    public AspectRatioImageView(Context context) {
        this(context, null);
    }

    public AspectRatioImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioView);
            orientation = a.getInt(R.styleable.AspectRatioView_orientation, ORIENTATION_HORIZONTAL);
            ratio = a.getFloat(R.styleable.AspectRatioView_ratio, 1.0f);
            a.recycle();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(orientation == ORIENTATION_HORIZONTAL) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() / ratio));
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) (getMeasuredHeight() * ratio), getMeasuredHeight());
        }
    }
}
