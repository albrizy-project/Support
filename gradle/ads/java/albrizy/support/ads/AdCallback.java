package albrizy.support.ads;

import android.support.annotation.Nullable;

interface AdCallback {

    void setAdType(int adType);
    void setAdType(AdType adType);
    void setAdListener(@Nullable AdListener listener);
    void setFailedVisibility(int visibility);
    void onResume();
    void onPause();
    void onDestroy();
}
