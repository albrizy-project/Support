package albrizy.support.ads;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class AdLoader {

    @Nullable
    static Map<String, AdResponse> adMap;
    static Set<OnResponseListener> onResponseListeners;
    static OkHttpClient client;
    static String adId;

    static {
        onResponseListeners = new HashSet<>();
    }

    interface OnResponseListener {
        void onResponse();
    }

    static class Task extends AsyncTask<Void, Void, Map<String, AdResponse>> {

        @Override
        @SuppressWarnings("ConstantConditions")
        protected Map<String, AdResponse> doInBackground(Void... voids) {
            if (client != null) {
                Request request = new Request.Builder()
                        .url("https://drive.google.com/uc?export=download&id=" + adId)
                        .header("Cache-Exp", "900")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
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
            AdLoader.adMap = result;
            for (AdLoader.OnResponseListener listener : onResponseListeners) {
                listener.onResponse();
            }
        }
    }
}
