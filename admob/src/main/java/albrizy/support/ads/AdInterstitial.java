package albrizy.support.ads;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

class AdInterstitial {

    @Nullable
    private InterstitialAd interstitial;
    private final Context context;
    private long hideDuration;
    private int requestCount;

    AdInterstitial(Context context) {
        this.context = context;
    }

    private void setupAd() {
        final String[] adUnits = MobileAds.instance.interstitialIds;
        final int index = new Random().nextInt(adUnits.length);
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(adUnits[index]);
        interstitial.setAdListener(adListener);
        adListener.onAdClosed();
    }

    void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    void show(boolean force) {
        if (interstitial == null) setupAd();
        if (hideDuration < System.currentTimeMillis()) {
            if (force) requestCount = MobileAds.instance.interstitialShowAfterClicks;
            if (requestCount >= MobileAds.instance.interstitialShowAfterClicks) {
                interstitial.show();
                requestCount = 0;
            } else requestCount++;
        }
    }

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdClosed() {
            if (interstitial != null) {
                interstitial.loadAd(MobileAds.getRequest());
            }
        }

        @Override
        public void onAdLeftApplication() {
            interstitial = null;
            hideDuration = (System.currentTimeMillis()
                    + MobileAds.instance.hideDuration);
        }
    };
}
