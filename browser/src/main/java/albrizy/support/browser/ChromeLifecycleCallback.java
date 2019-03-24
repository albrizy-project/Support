package albrizy.support.browser;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ChromeLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private Chrome chrome;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        chrome = new Chrome();
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        chrome.register(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        chrome.unregister(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}

}
