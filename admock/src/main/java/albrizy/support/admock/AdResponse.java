package albrizy.support.admock;

@SuppressWarnings("WeakerAccess")
class AdResponse {

    boolean isError;
    Item[] adItems;

    static class Item {

        String image;
        String link;
    }
}
