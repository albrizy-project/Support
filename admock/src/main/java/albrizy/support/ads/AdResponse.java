package albrizy.support.ads;

@SuppressWarnings("WeakerAccess")
class AdResponse {

    boolean isError;
    Item[] adItems;

    static class Item {

        String image;
        String link;
    }
}
