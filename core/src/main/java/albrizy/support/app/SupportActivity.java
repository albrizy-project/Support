package albrizy.support.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public abstract class SupportActivity extends AppCompatActivity {

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
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
}
