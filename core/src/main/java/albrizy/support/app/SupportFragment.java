package albrizy.support.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public abstract class SupportFragment extends Fragment {

    @LayoutRes
    protected abstract int getLayoutRes();
    private Unbinder binder;

    public boolean isFragmentSafe() {
        return getActivity() != null
                && getView() != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        binder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public final void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c);
        if (c.orientation == ORIENTATION_LANDSCAPE ||
            c.orientation == ORIENTATION_PORTRAIT) {
            onOrientationChanged(c, c.orientation == ORIENTATION_LANDSCAPE);
        }
    }

    protected void onOrientationChanged(Configuration c, boolean landscape) {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binder.unbind();
    }
}
