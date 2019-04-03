package albrizy.support.admock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Random;

import albrizy.support.admock.MobileAds.OnResponseListener;

@SuppressLint("AppCompatCustomView")
public class AdLayout extends ImageView implements OnResponseListener {

    public static final String TYPE_BANNER = "BANNER";
    public static final String TYPE_RECTANGLE = "RECTANGLE";
    public static final String TYPE_INTERSTITIAL = "INTERSTITIAL";
    public static final String TYPE_NATIVE = "NATIVE";

    @SuppressWarnings("WeakerAccess")
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_BANNER, TYPE_RECTANGLE, TYPE_INTERSTITIAL, TYPE_NATIVE})
    public @interface AdType {}

    @Nullable
    private AdListener adListener;
    private String adType;

    public AdLayout(Context context) {
        this(context, null);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MobileAds.onResponseListeners.add(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdLayout);
            setAdType(a.getInt(R.styleable.AdLayout_ad_type, 0));
            a.recycle();
        }
    }

    private void setAdType(int adType) {
        switch (adType) {
            case 3: setAdType(TYPE_NATIVE); break;
            case 2: setAdType(TYPE_INTERSTITIAL); break;
            case 1: setAdType(TYPE_RECTANGLE); break;
            default: setAdType(TYPE_BANNER); break;
        }
    }

    public void setAdType(@AdType String adType) {
        this.adType = adType;
    }

    public void setAdListener(@Nullable AdListener adListener) {
        this.adListener = adListener;
    }

    @SuppressWarnings("unchecked")
    private void setupAd(AdResponse adResponse) {
        if (adResponse != null && !adResponse.isError) {
            final int index = new Random().nextInt(adResponse.adItems.length);
            AdResponse.Item item = adResponse.adItems[index];
            Glide.with(getContext())
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(glideListener)
                    .dontAnimate()
                    .into(this);
            setOnClickListener(v -> MobileAds.onAdClick(getContext(), item));
        }
    }

    private boolean onAdRequested(boolean success) {
        setVisibility(success ? VISIBLE : INVISIBLE);
        if (adListener != null) {
            adListener.onAdLoaded(success);
        }
        return false;
    }

    public void onResume() {
        Map<String, AdResponse> ads = MobileAds.ads;
        if (ads != null) {
            setupAd(ads.get(adType));
        } else {
            MobileAds.Loader loader = new MobileAds.Loader();
            loader.execute();
        }
    }

    @Override
    public void onResponse() {
        onResume();
    }

    public void onPause() {}
    public void onDestroy() {
        MobileAds.onResponseListeners.remove(this);
    }

    private final RequestListener glideListener = new RequestListener() {
        @Override
        public boolean onResourceReady(Object o, Object oo, Target t, boolean b, boolean bb) {
            return onAdRequested(true);
        }

        @Override
        public boolean onException(Exception e, Object o, Target t, boolean b) {
            return onAdRequested(false);
        }
    };

    public interface AdListener {
        void onAdLoaded(boolean success);
    }
}
