package albrizy.support.ads;

import android.app.Application;
import android.content.res.Resources;
import android.support.annotation.StringRes;

@SuppressWarnings("WeakerAccess")
public class MobileAds {

    private static AdInterstitial interstitial;
    public static AdInterstitial getInterstitial() {
        return interstitial;
    }

    public static void initialize(Application app, Object client) {
        com.google.android.gms.ads.MobileAds.initialize(app, app.getString(R.string.ad_id));
        interstitial = new AdInterstitial(app);
    }

    static String[] resolveArrayRes(Resources res, @StringRes int string) {
        return res.getString(string).split(",");
    }
}
