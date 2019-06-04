package albrizy.support.adapter.loadable;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class Loadable extends OnScrollListener {

    public interface OnLoadListener {
        void onLoad();
    }

    private OnLoadListener listener;
    private boolean loadMoreEnabled = true;
    private boolean isLoading;
    private int dy;

    Loadable(OnLoadListener listener) {
        this.listener = listener;
    }

    void setListener(OnLoadListener listener) {
        this.listener = listener;
    }

    void setLoadMoreEnabled(boolean loadMoreEnabled) {
        this.loadMoreEnabled = loadMoreEnabled;
    }

    void notifyLoadingCompleted() {
        isLoading = false;
    }

    boolean isLoadMoreEnabled() {
        return loadMoreEnabled;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView r, int dx, int dy) {
        this.dy = dy;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onScrollStateChanged(@NonNull RecyclerView r, int state) {
        if (state == SCROLL_STATE_IDLE) {
            if (dy > 0) {
                if (loadMoreEnabled && listener != null) {
                    boolean bottom;
                    LayoutManager lm = r.getLayoutManager();
                    if (lm instanceof GridLayoutManager)
                        bottom  = ((GridLayoutManager) lm).findLastVisibleItemPosition()
                                >= lm.getItemCount() - 1;
                    else bottom  = ((LinearLayoutManager) lm).findLastVisibleItemPosition()
                            >= lm.getItemCount() - 1;
                    if (bottom && !isLoading) {
                        isLoading = true;
                        listener.onLoad();
                    }
                }
            }
        }
    }
}
