package albrizy.support.ads;

import android.content.Context;
import android.content.Intent;

import okhttp3.OkHttpClient;

@SuppressWarnings("WeakerAccess")
public class MobileAds {

    static MobileAds instance;

    public static MobileAds initialize(Context context, String adId) {
        instance = new MobileAds();
        instance.adId = adId;
        return instance;
    }

    public static void showInterstitial(Context context) {
        instance.show(context, false);
    }

    public static void showInterstitial(Context context, boolean forceShow) {
        instance.show(context, forceShow);
    }

    String adId;
    OkHttpClient client;
    private int interstitialShowAfterClicks;
    private int requestCount;

    private MobileAds() {}

    public MobileAds setId(String adId) {
        this.adId = adId;
        return this;
    }

    public MobileAds setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public MobileAds setInterstitialShowAfterClicks(int i) {
        this.interstitialShowAfterClicks = i;
        this.requestCount = i - 1;
        return this;
    }

    private void show(Context context, boolean force) {
        if (force) requestCount = interstitialShowAfterClicks;
        if (requestCount >= interstitialShowAfterClicks) {
            Intent intent = new Intent(context, AdActivity.class);
            context.startActivity(intent);
            requestCount = 0;
        } else requestCount++;
    }
}
