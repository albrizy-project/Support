package albrizy.support.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class RVAdapter<T> extends RecyclerView.Adapter<RVHolder> {

    @NonNull
    private List<T> items;
    protected final LayoutInflater inflater;

    public RVAdapter(@NonNull Context context, @NonNull List<T> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    public List<T> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(@NonNull List<T> items) {
        this.items = items;
    }

    public void addItems(@NonNull List<T> items) {
        this.items.addAll(items);
    }

    public void addItem(@NonNull T item) {
        this.items.add(item);
    }

    public void clear() {
        try {
            items.clear();
            notifyDataSetChanged();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            items.clear();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}
