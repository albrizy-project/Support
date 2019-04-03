package albrizy.support.ads;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;

@SuppressWarnings("WeakerAccess")
public class MobileAds {

    static MobileAds instance;

    public static MobileAds initialize(Context context, String adId) {
        com.google.android.gms.ads.MobileAds.initialize(context, adId);
        AdInterstitial interstitial = new AdInterstitial(context);
        instance = new MobileAds(interstitial);
        instance.adId = adId;
        return instance;
    }

    public static void showInterstitial() {
        instance.interstitial.show(false);
    }

    public static void showInterstitial(boolean forceShow) {
        instance.interstitial.show(forceShow);
    }

    String adId;
    String[] keywords;
    String[] deviceIds;
    String[] bannerIds;
    String[] interstitialIds;
    int interstitialShowAfterClicks;
    int hideDuration;

    private final AdInterstitial interstitial;

    private MobileAds(AdInterstitial interstitial) {
        this.interstitial = interstitial;
    }

    public MobileAds setId(String adId) {
        this.adId = adId;
        return this;
    }

    public MobileAds setBannerIds(String[] bannerIds) {
        this.bannerIds = bannerIds;
        return this;
    }

    public MobileAds setInterstitialIds(String[] interstitialIds) {
        this.interstitialIds = interstitialIds;
        return this;
    }

    public MobileAds setDeviceIds(String[] deviceIds) {
        this.deviceIds = deviceIds;
        return this;
    }

    public MobileAds setKeywords(String[] keywords) {
        this.keywords = keywords;
        return this;
    }

    public MobileAds setInterstitialShowAfterClicks(int i) {
        this.interstitial.setRequestCount(i - 1);
        this.interstitialShowAfterClicks = i;
        return this;
    }

    public MobileAds setHildeDuration(int i) {
        this.hideDuration = i;
        return this;
    }

    static AdRequest getRequest() {
        AdRequest.Builder builder = new AdRequest.Builder();
        for (String s : instance.deviceIds) builder.addTestDevice(s);
        for (String s : instance.keywords) builder.addKeyword(s);
        return builder.build();
    }

    interface OnAdClickListener {
        void onAdClick();
    }
}
