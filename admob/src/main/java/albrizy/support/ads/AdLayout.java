package albrizy.support.ads;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class AdLayout extends FrameLayout implements AdCallback, OnClickedChangeListener {

    @Nullable
    private AdView adView;
    private AdSize adSize;
    private final String[] adUnits;
    private boolean adLoaded;

    @Nullable
    private AdListener adListener;
    private int failedVisibility = View.INVISIBLE;

    private static final ClickHandler handler = new ClickHandler();

    public AdLayout(Context context) {
        this(context, null);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = context.getResources();
        adUnits = res.getStringArray(R.array.ad_banners);
        handler.register(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdLayout);
            setAdType(a.getInt(R.styleable.AdLayout_ad_type, 0));
            a.recycle();
        }
    }

    @Override
    public void setAdType(int adType) {
        this.adSize = adType == 1
                ? AdSize.MEDIUM_RECTANGLE
                : AdSize.SMART_BANNER;
    }

    @Override
    public void setAdType(AdType adType) {
        this.adSize = adType.equals(AdType.RECTANGLE)
                ? AdSize.MEDIUM_RECTANGLE
                : AdSize.SMART_BANNER;
    }

    @Override
    public void setAdListener(@Nullable AdListener listener) {
        this.adListener = listener;
    }

    @Override
    public void setFailedVisibility(int visibility) {
        this.failedVisibility = visibility;
    }

    @Override
    public void onClickedChange(boolean clicked) {
        if (clicked) {
            removeAllViews();
            adLoaded = false;
            if (adView != null) {
                adView.destroy();
                adView = null;
            }
        }
    }

    @Override
    public void onResume() {
        if (handler.isClicked()) return;
        if (adView == null)  {
            final int index = new Random().nextInt(adUnits.length);
            adView = new AdView(getContext());
            adView.setAdListener(listener);
            adView.setAdUnitId(adUnits[index]);
            adView.setAdSize(adSize);
            removeAllViews();
            addView(adView);
        }
        if (adLoaded) {
            adView.resume();
        } else {
            adView.loadAd(MobileAds.getInterstitial()
                    .requestAd());
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        handler.unregister(this);
        if (adView != null) {
            adView.destroy();
        }
    }

    private final com.google.android.gms.ads.AdListener listener =
            new com.google.android.gms.ads.AdListener() {

        @Override
        public void onAdLoaded() {
            if (adView != null) {
                adView.setVisibility(View.VISIBLE);
                adLoaded = true;
                if (adListener != null) {
                    adListener.onAdRequested(true, 0);
                }
            }
        }

        @Override
        public void onAdFailedToLoad(int code) {
            if (adView != null) {
                adView.setVisibility(failedVisibility);
                adLoaded = false;
                if (adListener != null) {
                    adListener.onAdRequested(false, code);
                }
            }
        }

        @Override
        public void onAdLeftApplication() {
            handler.handleClick();
            if (adListener != null) {
                adListener.onAdClicked();
            }
        }
    };
}
