package albrizy.support.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

@SuppressWarnings("WeakerAccess")
public class Click {

    private Click() {}

    private static final String URL_PLAYSTORE ="https://play.google.com/store/apps/details?id=";
    private static final String URL_DEV ="https://play.google.com/store/apps/dev?id=";

    public static final String EXTRA_KEY = "extra_key";
    public static final String EXTRA_ITEM = "extra_item";

    public static void rateApp(Context context, String appId) {
        Click.openAppInfo(context, appId);
    }

    public static void openAppInfo(Context context, String appId) {
        Click.open(context, (URL_PLAYSTORE  + appId));
    }

    public static void openAppDev(Context context, String devId) {
        Click.open(context, (URL_DEV  + devId));
    }

    public static void openLink(Context context, String link) {
        open(context, link);
    }

    private static void open(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= 21) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                    | Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            );
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                    | Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            );
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
