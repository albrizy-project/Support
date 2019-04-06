package albrizy.support.ads;

import android.app.Application;

import okhttp3.OkHttpClient;

@SuppressWarnings("WeakerAccess")
public class MobileAds {

    private static AdInterstitial interstitial;
    public static AdInterstitial getInterstitial() {
        return interstitial;
    }

    public static void initialize(Application app, Object client) {
        AdLoader.adId = app.getString(R.string.ad_id);
        AdLoader.client = (OkHttpClient) client;
        interstitial = new AdInterstitial(app);
    }
}
