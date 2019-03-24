package albrizy.support.browser;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;

public class Browser {

    private Browser() {}

    public static void open(Context context, String url) {
        open(context, url, new CustomTabsIntent.Builder()
                .setShowTitle(false)
                .setStartAnimations(context, R.anim.anim_sheet_show, 0)
                .setExitAnimations(context, R.anim.anim_sheet_hide, 0)
                .enableUrlBarHiding()
                .build());
    }

    public static void open(Context context, String url, CustomTabsIntent intent) {
        String packageName = ChromeHelper.getPackageName(context);
        if (packageName == null) {
            openExternal(context, url);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.intent.putExtra(
                        Intent.EXTRA_REFERRER, Uri.parse(
                        Intent.URI_ANDROID_APP_SCHEME + "//" + context.getPackageName()));
            }

            intent.intent.setPackage(packageName);
            intent.launchUrl(context,  Uri.parse(url));
        }
    }

    private static void openExternal(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (Build.VERSION.SDK_INT >= 21)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                    | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                    | Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            );
        else intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ignored) {}
    }
}
