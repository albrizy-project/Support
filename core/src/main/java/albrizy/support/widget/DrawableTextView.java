package albrizy.support.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import albrizy.support.R;

public class DrawableTextView extends AppCompatTextView {

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, this);
    }

    @SuppressLint("CustomViewStyleable")
    static void init(Context context, AttributeSet attrs, TextView view) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableView);
        int start = a.getResourceId(R.styleable.DrawableView_dv_drawableStart, 0);
        int top = a.getResourceId(R.styleable.DrawableView_dv_drawableTop, 0);
        int end = a.getResourceId(R.styleable.DrawableView_dv_drawableEnd, 0);
        int bottom = a.getResourceId(R.styleable.DrawableView_dv_drawableBottom, 0);
        a.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    start != 0 ? AppCompatResources.getDrawable(context, start) : null,
                    top != 0 ? AppCompatResources.getDrawable(context, top) : null,
                    end != 0 ? AppCompatResources.getDrawable(context, end) : null,
                    bottom != 0 ? AppCompatResources.getDrawable(context, bottom) : null);
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(
                    start != 0 ? AppCompatResources.getDrawable(context, start) : null,
                    top != 0 ? AppCompatResources.getDrawable(context, top) : null,
                    end != 0 ? AppCompatResources.getDrawable(context, end) : null,
                    bottom != 0 ? AppCompatResources.getDrawable(context, bottom) : null);
        }
    }
}
