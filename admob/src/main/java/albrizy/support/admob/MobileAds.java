package albrizy.support.admob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class MobileAds {

    @SuppressLint("StaticFieldLeak")
    private static MobileAds instance;

    public static void initialize(Context applicationContext) {
        String adId = applicationContext.getString(R.string.admob_id);
        com.google.android.gms.ads.MobileAds.initialize(applicationContext, adId);
        instance = new MobileAds(applicationContext);
    }

    public static void showInterstitial() {
        instance.show(false);
    }

    public static void showInterstitial(boolean forceShow) {
        instance.show(forceShow);
    }

    @Nullable
    private InterstitialAd interstitial;
    private final String[] adUnits;
    private final String[] adKeywords;
    private final String[] adTestDevices;
    private final Context context;
    private final int showAfterClicks;
    private final int defaultHideDuration;
    private long hideDuration;
    private int requestCount;

    private MobileAds(Context applicationContext) {
        final Resources resources = applicationContext.getResources();
        adUnits = resources.getStringArray(R.array.admob_interstitial_ids);
        adKeywords = resources.getStringArray(R.array.admob_keywords);
        adTestDevices = resources.getStringArray(R.array.admob_test_device_ids);
        defaultHideDuration = resources.getInteger(R.integer.admob_hide_duration);
        showAfterClicks = resources.getInteger(R.integer.admob_interstitial_show_after_clicks);
        requestCount = showAfterClicks - 1;
        context = applicationContext;
    }

    private void setupAd() {
        final int index = new Random().nextInt(adUnits.length);
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(adUnits[index]);
        interstitial.setAdListener(adListener);
        adListener.onAdClosed();
    }

    private void show(boolean force) {
        if (interstitial == null) setupAd();
        if (hideDuration < System.currentTimeMillis()) {
            if (force) requestCount = showAfterClicks;
            if (requestCount >= showAfterClicks) {
                interstitial.show();
                requestCount = 0;
            } else requestCount++;
        }
    }

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdClosed() {
            if (interstitial != null) {
                interstitial.loadAd(getRequest(
                        adTestDevices,
                        adKeywords
                ));
            }
        }

        @Override
        public void onAdLeftApplication() {
            interstitial = null;
            hideDuration = (System.currentTimeMillis()
                    + defaultHideDuration);
        }
    };

    public static AdRequest getRequest(@NonNull String[] devices, @NonNull String[] keywords) {
        AdRequest.Builder builder = new AdRequest.Builder();
        for (String s : devices) builder.addTestDevice(s);
        for (String s : keywords) builder.addKeyword(s);
        return builder.build();
    }

    interface OnAdClickListener {
        void onAdClick();
    }
}
