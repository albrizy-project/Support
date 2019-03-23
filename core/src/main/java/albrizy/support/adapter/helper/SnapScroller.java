package albrizy.support.adapter.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

public class SnapScroller extends LinearSmoothScroller {

    public static final int SNAP_TO_START = LinearSmoothScroller.SNAP_TO_START;
    public static final int SNAP_TO_END = LinearSmoothScroller.SNAP_TO_END;
    private static final float MILLISECONDS_PER_INCH = 100f;

    @NonNull
    private final Context context;
    private final int snapMode;
    private float millisecondsPerPixel;

    public SnapScroller(@NonNull Context context, int snapMode) {
        super(context);
        this.context = context;
        this.snapMode = snapMode;
    }

    @Override
    protected void onStart() {
        super.onStart();
        final LayoutManager manager = getLayoutManager();
        if (manager != null) {
            View firstView = manager.getChildAt(0);
            int firstViewPosition = getChildPosition(firstView);
            int intermediateViewCount = Math.abs(firstViewPosition - getTargetPosition());
            int dpi = context.getResources().getDisplayMetrics().densityDpi;
            millisecondsPerPixel = MILLISECONDS_PER_INCH
                    / (float) Math.sqrt(intermediateViewCount)
                    / dpi;
        }
    }

    @Override
    protected int calculateTimeForScrolling(int dx) {
        return (int) Math.ceil(Math.abs(dx)
                * millisecondsPerPixel
        );
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return snapMode;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return snapMode;
    }
}
