package albrizy.support.browser;

import android.app.Activity;
import android.content.ComponentName;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

class Chrome {

    @Nullable private CustomTabsClient client;
    @Nullable private CustomTabsSession session;
    @Nullable private CustomTabsServiceConnection connection;

    Chrome() {}

    @Nullable
    public CustomTabsSession getSession() {
        if (client != null && session == null) {
            session = client.newSession(null);
        }
        return session;
    }

    private void initClient(Activity activity, final String pkgName) {
        connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                Chrome.this.client = client;
                Chrome.this.client.warmup(0L);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                client = null;
            }
            @Override
            public void onBindingDied(ComponentName name) {
                client = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(activity, pkgName, connection);
    }

    void register(Activity activity) {
        if (client == null) {
            String pkgName = ChromeHelper.getPackageName(activity);
            if (pkgName != null) {
                initClient(activity, pkgName);
            }
        }
    }

    void unregister(Activity activity) {
        if (connection != null) {
            activity.unbindService(connection);
            connection = null;
            session = null;
            client = null;
        }
    }
}
