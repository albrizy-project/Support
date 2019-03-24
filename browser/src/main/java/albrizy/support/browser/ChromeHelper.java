package albrizy.support.browser;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.GET_RESOLVED_FILTER;
import static android.support.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION;

class ChromeHelper {

    private static final String STABLE_VERSION = "com.android.chrome";
    private static final String BETA_VERSION = "com.chrome.beta";
    private static final String DEV_VERSION = "com.chrome.dev";
    private static final String LOCAL_VERSION = "com.google.android.apps.chrome";

    private static String packageName;
    private ChromeHelper() {}

    @Nullable
    public static String getPackageName(Context context) {
        if (packageName == null) {
            packageName = resolvePackageName(context);
        }
        return packageName;
    }

    @Nullable
    private static String resolvePackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String defaultViewHandlerPackageName = null;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"));
        ResolveInfo defaultViewHandlerInfo = manager.resolveActivity(intent, 0);
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName;
        }

        List<String> packageNames = resolvePackageName(manager, intent);
        if (!packageNames.isEmpty()) {
            if (packageNames.size() == 1) {
                return packageNames.get(0);
            } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                    && !hasSpecializedHandlerIntents(manager, intent)
                    && packageNames.contains(defaultViewHandlerPackageName)) {
                return defaultViewHandlerPackageName;
            } else if (packageNames.contains(STABLE_VERSION)) {
                return STABLE_VERSION;
            } else if (packageNames.contains(BETA_VERSION)) {
                return BETA_VERSION;
            } else if (packageNames.contains(DEV_VERSION)) {
                return DEV_VERSION;
            } else if (packageNames.contains(LOCAL_VERSION)) {
                return LOCAL_VERSION;
            }
        }
        return null;
    }

    private static List<String> resolvePackageName(PackageManager manager, Intent intent) {
        List<ResolveInfo> resolvedActivityList = manager.queryIntentActivities(intent, 0);
        List<String> packageNames = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction(ACTION_CUSTOM_TABS_CONNECTION);
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (manager.resolveService(serviceIntent, 0) != null) {
                packageNames.add(info.activityInfo.packageName);
            }
        }
        return packageNames;
    }

    private static boolean hasSpecializedHandlerIntents(PackageManager manager, Intent intent) {
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, GET_RESOLVED_FILTER);
        if (infos == null || infos.isEmpty()) {
            return false;
        }

        for (ResolveInfo info : infos) {
            IntentFilter filter = info.filter;
            if (filter == null) continue;
            if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) {
                continue;
            }
            if (info.activityInfo == null) continue;
            return true;
        }
        return false;
    }

}
