package albrizy.support.ads;

import android.app.Application;

@SuppressWarnings("WeakerAccess")
public class MobileAds {

    private static AdInterstitial interstitial;
    public static AdInterstitial getInterstitial() {
        return interstitial;
    }

    public static void initialize(Application app, Object arg) {
        com.google.android.gms.ads.MobileAds.initialize(app, app.getString(R.string.ad_id));
        interstitial = new AdInterstitial(app);
    }
}
