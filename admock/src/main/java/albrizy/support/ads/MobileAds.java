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
        showInterstitial(context, false);
    }

    public static void showInterstitial(Context context, boolean forceShow) {
        Intent intent = new Intent(context, AdActivity.class);
        context.startActivity(intent);
    }

    String adId;
    OkHttpClient client;

    private MobileAds() {}

    public MobileAds setId(String adId) {
        this.adId = adId;
        return this;
    }

    public MobileAds setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }
}
