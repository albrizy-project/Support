package albrizy.support.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Gravity;
import android.view.View;

@SuppressWarnings("SameParameterValue")
public class Snapper extends LinearSnapHelper {

    private OrientationHelper verticalHelper;
    private OrientationHelper horizontalHelper;
    private final int gravity;
    private boolean snapLastItem;

    public Snapper(int gravity, boolean snapLastItem) {
        if (gravity != Gravity.START
                && gravity != Gravity.END
                && gravity != Gravity.BOTTOM
                && gravity != Gravity.TOP) {
            throw new IllegalArgumentException(
                    "Invalid gravity value. Use START | END | BOTTOM | TOP constants"
            );
        }
        this.gravity = gravity;
        this.snapLastItem = snapLastItem;
    }

    public void setSnapLastItem(boolean snapLastItem) {
        this.snapLastItem = snapLastItem;
    }

    private OrientationHelper getVerticalHelper(LayoutManager manager) {
        if (verticalHelper == null) {
            verticalHelper = OrientationHelper.createVerticalHelper(manager);
        }
        return verticalHelper;
    }

    private OrientationHelper getHorizontalHelper(LayoutManager manager) {
        if (horizontalHelper == null) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(manager);
        }
        return horizontalHelper;
    }

    private int distanceToStart(View target, OrientationHelper helper, boolean fromEnd) {
        return helper.getDecoratedStart(target) - helper.getStartAfterPadding();
    }

    private int distanceToEnd(View target, OrientationHelper helper, boolean fromStart) {
        return helper.getDecoratedEnd(target) - helper.getEndAfterPadding();
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager manager, @NonNull View target) {
        int[] out = new int[2];
        if (manager.canScrollHorizontally()) {
            if (gravity == Gravity.START)
                out[0] = distanceToStart(target, getHorizontalHelper(manager), false);
            else out[0] = distanceToEnd(target, getHorizontalHelper(manager), false);

        } else if (manager.canScrollVertically()) {
            if (gravity == Gravity.TOP)
                out[1] = distanceToStart(target, getVerticalHelper(manager), false);
            else out[1] = distanceToEnd(target, getVerticalHelper(manager), false);
        }
        return out;
    }

    @Override
    public View findSnapView(LayoutManager manager) {
        View snapView = null;
        if (manager instanceof LinearLayoutManager) {
            switch (gravity) {
                case Gravity.START:
                    snapView = findStartView(manager, getHorizontalHelper(manager));
                    break;
                case Gravity.END:
                    snapView = findEndView(manager, getHorizontalHelper(manager));
                    break;
                case Gravity.TOP:
                    snapView = findStartView(manager, getVerticalHelper(manager));
                    break;
                case Gravity.BOTTOM:
                    snapView = findEndView(manager, getVerticalHelper(manager));
                    break;
            }
        }
        return snapView;
    }

    private View findStartView(LayoutManager layoutManager, OrientationHelper helper) {
        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (firstChild == RecyclerView.NO_POSITION)
                return null;

            View child = layoutManager.findViewByPosition(firstChild);
            float visibleWidth;

            visibleWidth = (float)
                    helper.getDecoratedEnd(child) /
                    helper.getDecoratedMeasurement(child);

            boolean endOfList = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1;

            if (visibleWidth > 0.5f && !endOfList) {
                return child;
            } else if (snapLastItem && endOfList) {
                return child;
            } else if (endOfList) {
                return null;
            } else {
                return layoutManager.findViewByPosition(firstChild + 1);
            }
        }

        return null;
    }

    private View findEndView(LayoutManager manager, OrientationHelper helper) {
        if (manager instanceof LinearLayoutManager) {
            int lastChild = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            if (lastChild == RecyclerView.NO_POSITION) return null;

            View child = manager.findViewByPosition(lastChild);
            float visibleWidth;

            visibleWidth = (float) (
                    helper.getTotalSpace() - helper.getDecoratedStart(child)) /
                    helper.getDecoratedMeasurement(child);

            boolean startOfList = ((LinearLayoutManager) manager)
                    .findFirstCompletelyVisibleItemPosition() == 0;

            if (visibleWidth > 0.5f && !startOfList) {
                return child;
            } else if (snapLastItem && startOfList) {
                return child;
            } else if (startOfList) {
                return null;
            } else {
                return manager.findViewByPosition(lastChild - 1);
            }
        }
        return null;
    }
}
