package com.albrizy.support.admob;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

@SuppressWarnings("WeakerAccess, UnnecessaryLocalVariable")
public class Admob {

    static String deviceId;
    static String[] keywords;
    static String[] bannerIds;
    static String[] interstitialIds;
    static int interstitialShowAfterClicks;
    static int hideDuration;

    static AdRequest requestAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if (deviceId != null) builder.addTestDevice(deviceId);
        if (keywords != null) {
            for (String key : keywords) {
                builder.addKeyword(key);
            }
        }
        return builder.build();
    }

    public static Config initialize(Context context, String admobId) {
        MobileAds.initialize(context, admobId);
        Config config = new Config();
        return config;
    }

    public static class Config {

        private Config() {}

        public Config setDeviceId(String deviceId) {
            Admob.deviceId = deviceId;
            return this;
        }

        public Config setKeywords(String[] keywords) {
            Admob.keywords = keywords;
            return this;
        }

        public Config setBannerIds(String[] bannerIds) {
            Admob.bannerIds = bannerIds;
            return this;
        }

        public Config setInterstitialIds(String[] interstitialIds) {
            Admob.interstitialIds = interstitialIds;
            return this;
        }

        public Config setInterstitialShowAfterClicks(int interstitialShowAfterClicks) {
            Admob.interstitialShowAfterClicks = interstitialShowAfterClicks;
            return this;
        }

        public Config setHideDuration(int hideDuration) {
            Admob.hideDuration = hideDuration;
            return this;
        }

        public void apply() {}
    }
}
