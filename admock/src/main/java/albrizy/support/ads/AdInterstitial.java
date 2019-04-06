package albrizy.support.ads;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
public class AdInterstitial {

    @Nullable
    AdListener adListener;
    private final int showAfterClicks;
    private int requestCount;

    public AdInterstitial(Context context) {
        Resources res = context.getResources();
        this.showAfterClicks = res.getInteger(R.integer.ad_interstitial_show_after_clicks);
    }

    public void setAdListener(@Nullable AdListener listener) {
        this.adListener = listener;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public void show(Context context) {
        show(context, false);
    }

    public void show(Context context, boolean force) {
        if (force) requestCount = showAfterClicks;
        if (requestCount >= showAfterClicks) {
            Intent intent = new Intent(context, AdActivity.class);
            context.startActivity(intent);
            requestCount = 0;
        } else requestCount++;
    }
}
