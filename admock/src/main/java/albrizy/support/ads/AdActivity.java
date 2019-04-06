package albrizy.support.ads;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class AdActivity extends AppCompatActivity {

    private AdLayout adLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admock_activity);
        findViewById(R.id.ad_close).setOnClickListener(v -> supportFinishAfterTransition());
        adLayout = findViewById(R.id.ad_layout);
        adLayout.setAdListener(MobileAds.getInterstitial().adListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adLayout.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adLayout.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adLayout.onDestroy();
    }
}
