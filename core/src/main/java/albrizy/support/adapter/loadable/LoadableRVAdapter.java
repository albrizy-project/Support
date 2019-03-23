package albrizy.support.adapter.loadable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import albrizy.support.R;
import albrizy.support.adapter.RVAdapter;
import albrizy.support.adapter.RVHolder;

@SuppressWarnings("WeakerAccess")
public abstract class LoadableRVAdapter<T> extends RVAdapter<T> {

    private final Loadable loadable;

    public LoadableRVAdapter(@NonNull Context context, @NonNull List<T> items) {
        this(context, items, null);
    }

    public LoadableRVAdapter(@NonNull Context context,
                             @NonNull List<T> items,
                             @Nullable Loadable.OnLoadListener listener) {
        super(context, items);
        loadable = new Loadable();
        loadable.listener = listener;
    }

    public void setListener(@Nullable Loadable.OnLoadListener listener) {
        loadable.listener = listener;
    }

    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        loadable.loadMoreEnabled = loadMoreEnabled;
    }

    public void notifyLoadingCompleted() {
        loadable.isLoading = false;
    }

    public int getLoadingType() {
        return R.layout.loading_holder;
    }

    protected abstract int getItemType(int position);

    @Override
    public final int getItemViewType(int position) {
        return position >= getItems().size()
                ? getLoadingType()
                : getItemType(position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recycler) {
        recycler.addOnScrollListener(loadable);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recycler) {
        recycler.removeOnScrollListener(loadable);
    }

    @NonNull
    @Override
    public final RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return type == getLoadingType()
                ? new RVHolder(inflater.inflate(type, parent, false))
                : onCreateHolder(parent, type);
    }

    @NonNull
    protected abstract RVHolder onCreateHolder(@NonNull ViewGroup parent, int type);
    protected abstract void onBindHolder(@NonNull RVHolder base, int position);

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, int position) {
        if (holder.getItemViewType() == getLoadingType()) {
            return;
        }
        onBindHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return loadable.loadMoreEnabled
                ? getItems().size() + 1
                : getItems().size();
    }
}
