package albrizy.support.ads;

import android.content.Context;

import okhttp3.OkHttpClient;

public class MobileAds {

    static MobileAds instance;

    public static MobileAds initialize(Context context, String adId) {
        instance = new MobileAds();
        instance.adId = adId;
        return instance;
    }

    String adId;
    OkHttpClient client;

    private MobileAds() {}

    MobileAds setId(String adId) {
        this.adId = adId;
        return this;
    }

    MobileAds setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }
}
