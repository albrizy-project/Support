package albrizy.support.ads;

public interface AdListener {

    void onAdRequested(boolean success, int errorCode);
    void onAdClicked();
}
