package albrizy.support.ads;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

public class AdInterstitial {

    @Nullable
    private AdListener adListener;
    private final AdRequest.Builder request;
    private final ClickHandler handler;
    private final String[] adUnits;
    private final int showAfterClicks;
    final int hideDuration;

    @Nullable
    private InterstitialAd interstitial;
    private int requestCount;

    public AdInterstitial(Context context) {
        Resources res = context.getResources();
        this.handler = new ClickHandler();
        this.request = new AdRequest.Builder();
        this.adUnits = MobileAds.resolveArrayRes(res, R.string.ad_interstitial);
        this.showAfterClicks = res.getInteger(R.integer.ad_interstitial_show_after_clicks);
        this.hideDuration = res.getInteger(R.integer.ad_hide_duration);

        for (String s : MobileAds.resolveArrayRes(res, R.string.ad_devices)) request.addTestDevice(s);
        for (String s : MobileAds.resolveArrayRes(res, R.string.ad_keywords)) request.addKeyword(s);
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
        if (interstitial == null) {
            final int index = new Random().nextInt(adUnits.length);
            interstitial = new InterstitialAd(context);
            interstitial.setAdUnitId(adUnits[index]);
            interstitial.setAdListener(listener);
            interstitial.loadAd(requestAd());
        }
        if (handler.isClicked()) return;
        if (force) requestCount = showAfterClicks;
        if (requestCount >= showAfterClicks) {
            interstitial.show();
            requestCount = 0;
        } else requestCount++;
    }

    AdRequest requestAd() {
        return request.build();
    }

    private final com.google.android.gms.ads.AdListener listener =
            new com.google.android.gms.ads.AdListener() {
        @Override
        public void onAdLoaded() {
            if (adListener != null) {
                adListener.onAdRequested(true, 0);
            }
        }

        @Override
        public void onAdFailedToLoad(int i) {
            if (adListener != null) {
                adListener.onAdRequested(false, i);
            }
        }

        @Override
        public void onAdClosed() {
            if (interstitial != null) {
                interstitial.loadAd(requestAd());
            }
        }

        @Override
        public void onAdLeftApplication() {
            interstitial = null;
            handler.handleClick();
            if (adListener != null) {
                adListener.onAdClicked();
            }
        }
    };
}
