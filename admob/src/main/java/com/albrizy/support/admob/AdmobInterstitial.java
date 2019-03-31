package com.albrizy.support.admob;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import static com.albrizy.support.admob.Admob.*;

public class AdmobInterstitial implements AdLayout {

    @Nullable
    private InterstitialAd interstitial;
    private final Context context;
    private int requestCount;

    public AdmobInterstitial(Context context) {
        this.requestCount = interstitialShowAfterClicks;
        this.context = context;
    }

    @Override
    public void onCreate() {
        final int index = new Random().nextInt(interstitialIds.length);
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(interstitialIds[index]);
        interstitial.setAdListener(listener);
    }

    @Override
    public void onResume() {
        if (interstitial == null) onCreate();
        if (!interstitial.isLoaded()) {
            interstitial.loadAd(requestAd());
        }

        if (requestCount >= interstitialShowAfterClicks) {
            interstitial.show();
            requestCount = 0;
        } else requestCount++;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    private final AdListener listener = new AdListener() {
        @Override
        public void onAdLeftApplication() {}
    };
}
