package albrizy.support.ads;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

@SuppressLint("AppCompatCustomView")
public class AdLayout extends ImageView implements AdCallback, AdLoader.OnResponseListener {

    @Nullable
    private AdListener adListener;
    private AdType adType;
    private int failedVisibility = View.INVISIBLE;

    public AdLayout(Context context) {
        this(context, null);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AdLoader.onResponseListeners.add(this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdLayout);
            setAdType(a.getInt(R.styleable.AdLayout_ad_type, 0));
            a.recycle();
        }
    }

    @Override
    public void setAdType(int adType) {
        switch (adType) {
            case 3: setAdType(AdType.NATIVE); break;
            case 2: setAdType(AdType.INTERSTITIAL); break;
            case 1: setAdType(AdType.RECTANGLE); break;
            default: setAdType(AdType.BANNER); break;
        }
    }

    @Override
    public void setAdType(AdType adType) {
        this.adType = adType;
    }

    @Override
    public void setAdListener(@Nullable AdListener listener) {
        this.adListener = listener;
    }

    @Override
    public void setFailedVisibility(int visibility) {
        this.failedVisibility = visibility;
    }

    private boolean onAdRequested(boolean success) {
        setVisibility(success ? VISIBLE : failedVisibility);
        if (adListener != null) {
            adListener.onAdRequested(success, 0);
        }
        return false;
    }

    @Override
    public void onResponse() {
        onResume();
    }

    @Override
    public void onResume() {
        final Map<String, AdResponse> ads = AdLoader.adMap;
        if (ads != null) {
            AdResponse adResponse = ads.get(adType.name());
            if (adResponse != null && !adResponse.isError) {
                final int index = new Random().nextInt(adResponse.adItems.length);
                AdResponse.Item item = adResponse.adItems[index];
                //noinspection unchecked
                Glide.with(getContext())
                        .load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(glideListener)
                        .dontAnimate()
                        .into(this);
                setOnClickListener(v -> {
                    onAdClick(getContext(), item);
                    if (adListener != null) {
                        adListener.onAdClicked();
                    }
                });
            }
        } else {
            AdLoader.Task loader = new AdLoader.Task();
            loader.execute();
        }
    }

    @Override
    public void onPause() {}

    @Override
    public void onDestroy() {
        AdLoader.onResponseListeners.remove(this);
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

    @SuppressWarnings("unchecked")
    private static void onAdClick(Context context, AdResponse.Item item) {
        try {
            Class clazz = Class.forName("albrizy.support.browser.Browser");
            Method method = clazz.getDeclaredMethod("open", Context.class, String.class);
            method.invoke(null, context, item.link);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(item.link));
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
}
