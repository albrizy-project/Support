package albrizy.support.adapter.loadable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class Loadable extends RecyclerView.OnScrollListener {

    @Nullable
    OnLoadListener listener;
    boolean loadMoreEnabled;
    boolean isLoading;
    private int dy;

    Loadable() {}

    @Override
    public void onScrolled(@NonNull RecyclerView recycler, int dx, int dy) {
        this.dy = dy;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onScrollStateChanged(@NonNull RecyclerView recycler, int newState) {
        if (listener == null) return;
        if (newState == SCROLL_STATE_IDLE && dy > 0 && loadMoreEnabled) {
            boolean bottom;
            LayoutManager lm = recycler.getLayoutManager();
            if (lm instanceof GridLayoutManager) {
                bottom = ((GridLayoutManager) lm).findLastVisibleItemPosition()
                        >= lm.getItemCount() - 1;
            } else {
                bottom = ((LinearLayoutManager) lm).findLastVisibleItemPosition()
                        >= lm.getItemCount() - 1;
            }
            if (bottom && !isLoading) {
                isLoading = true;
                listener.onLoad();
            }
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }
}
