package albrizy.support.ads;

import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

class ClickHandler {

    private final Set<OnClickedChangeListener> listeners;
    private final Handler handler;
    private boolean clicked;

    ClickHandler() {
        listeners = new HashSet<>();
        handler = new Handler();
    }

    void register(OnClickedChangeListener listener) {
        listeners.add(listener);
    }

    void unregister(OnClickedChangeListener listener) {
        listeners.remove(listener);
    }

    boolean isClicked() {
        return clicked;
    }

    void handleClick() {
        clicked = true;
        notifyOnClickedChangeListener();
        handler.postDelayed(() -> {
            clicked = false;
            notifyOnClickedChangeListener();
        }, MobileAds.getInterstitial().hideDuration);
    }

    private void notifyOnClickedChangeListener() {
        for (OnClickedChangeListener l : listeners) {
            l.onClickedChange(clicked);
        }
    }
}
