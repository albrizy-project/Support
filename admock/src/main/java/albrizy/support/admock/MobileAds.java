package albrizy.support.admock;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MobileAds {

    @Nullable
    static Map<String, AdResponse> ads;
    static Set<OnResponseListener> onResponseListeners;
    private static OkHttpClient okHttpClient;
    private static String adId;

    public static void initialize(Context context, OkHttpClient client) {
        onResponseListeners = new HashSet<>();
        okHttpClient = client;
        adId = context.getString(R.string.admock_id);
    }

    @SuppressWarnings("unchecked")
    static void onAdClick(Context context, AdResponse.Item item) {
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

    interface OnResponseListener {
        void onResponse();
    }

    static class Loader extends AsyncTask<Void, Void, Map<String, AdResponse>> {

        private static final String URL = "http://drive.google.com/uc?export=download&id=";

        @Override
        @SuppressWarnings("ConstantConditions")
        protected Map<String, AdResponse> doInBackground(Void... voids) {
            if (okHttpClient != null) {
                Request request = new Request.Builder()
                        .url(URL + adId)
                        .header("Cache-Exp", "900")
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    JSONObject object = new JSONObject(response.body().string());
                    return doParse(object);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private Map<String, AdResponse> doParse(JSONObject object) throws JSONException {
            Map<String, AdResponse> result = new HashMap<>();
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()) {
                AdResponse adResponse = new AdResponse();
                String key = iterator.next();
                JSONObject obj = object.getJSONObject(key);
                JSONArray array = obj.getJSONArray("adItems");
                adResponse.isError = obj.getBoolean("isError");
                adResponse.adItems = new AdResponse.Item[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    AdResponse.Item item = new AdResponse.Item();
                    item.image = o.getString("image");
                    item.link = o.getString("link");
                    adResponse.adItems[i] = item;
                }
                result.put(key, adResponse);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Map<String, AdResponse> result) {
            for (OnResponseListener listener : onResponseListeners) {
                listener.onResponse();
            }
        }
    }
}
