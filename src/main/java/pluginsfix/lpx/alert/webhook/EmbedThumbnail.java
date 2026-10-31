package pluginsfix.lpx.alert.webhook;
class EmbedThumbnail {
    private final String url;
    EmbedThumbnail(String url) {
        this.url = url;
    }
    String getUrl() {
        return url;
    }
    String pv(Object[] ignored) {
        return getUrl();
    }
}
