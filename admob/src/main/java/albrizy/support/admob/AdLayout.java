package albrizy.support.admob;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import albrizy.support.admob.MobileAds.OnAdClickListener;

public class AdLayout extends FrameLayout implements OnAdClickListener {

    @Nullable
    private AdView adView;
    private AdSize adSize;
    private final String[] adUnits;
    private final String[] adKeywords;
    private final String[] adTestDevices;
    private final int adHideDuration;
    private boolean adLoaded;
    private boolean adClicked;
    private boolean destroyed;

    @Nullable
    private OnLoadListener listener;
    private static Set<OnAdClickListener> onAdClickListeners = new HashSet<>();

    public AdLayout(Context context) {
        this(context, null);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources resources = getResources();
        adUnits = resources.getStringArray(R.array.admob_banner_ids);
        adKeywords = resources.getStringArray(R.array.admob_keywords);
        adTestDevices = resources.getStringArray(R.array.admob_test_device_ids);
        adHideDuration = resources.getInteger(R.integer.admob_hide_duration);
        onAdClickListeners.add(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdLayout);
            setAdType(a.getInt(R.styleable.AdLayout_ad_type, 0));
            a.recycle();
        }
    }

    private void setupAd() {
        final int index = new Random().nextInt(adUnits.length);
        adView = new AdView(getContext());
        adView.setAdListener(adListener);
        adView.setAdUnitId(adUnits[index]);
        adView.setAdSize(adSize);
        removeAllViews();
        addView(adView);
    }

    private void setAdType(int adType) {
        this.adSize = adType == 1
                ? AdSize.MEDIUM_RECTANGLE
                : AdSize.SMART_BANNER;
    }

    public void setAdType(AdSize adSize) {
        this.adSize = adSize;
    }

    public void setOnLoadListener(@Nullable OnLoadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAdClick() {
        adLoaded = false;
        adClicked = true;
        removeAllViews();
        postDelayed(() -> {
            adView = null;
            adClicked = false;
            if (!destroyed) {
                onResume();
            }
        }, adHideDuration);
    }

    public void onResume() {
        if (adClicked) return;
        if (adView == null) setupAd();
        if (adLoaded) {
            adView.resume();
        } else adView.loadAd(MobileAds.getRequest(
                adTestDevices,
                adKeywords
        ));
    }

    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
    }

    public void onDestroy() {
        onAdClickListeners.remove(this);
        this.destroyed = true;
        if (adView != null) {
            adView.setAdListener(null);
            adView.destroy();
            adView = null;
        }
    }

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            if (adView != null) {
                adView.setVisibility(View.VISIBLE);
                adLoaded = true;
                if (listener != null) {
                    listener.onAdLoaded();
                }
            }
        }
        @Override
        public void onAdFailedToLoad(int code) {
            if (adView != null) {
                adView.setVisibility(View.INVISIBLE);
                adLoaded = false;
                if (listener != null) {
                    listener.onAdFailed(code);
                }
            }
        }
        @Override
        public void onAdLeftApplication() {
            for (OnAdClickListener listener : onAdClickListeners) {
                listener.onAdClick();
            }
        }
    };

    public interface OnLoadListener {
        void onAdLoaded();
        void onAdFailed(int code);
    }
}
