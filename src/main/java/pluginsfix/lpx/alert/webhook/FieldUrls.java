package pluginsfix.lpx.alert.webhook;
class FieldUrls {
    private final String titleUrl;
    private final String descriptionUrl;
    private final String authorUrl;
    FieldUrls(String titleUrl, String descriptionUrl, String authorUrl) {
        this.titleUrl = titleUrl;
        this.descriptionUrl = descriptionUrl;
        this.authorUrl = authorUrl;
    }
    String getTitleUrl() {
        return titleUrl;
    }
    String getDescriptionUrl() {
        return descriptionUrl;
    }
    String getAuthorUrl() {
        return authorUrl;
    }
    String EC(Object[] ignored) {
        return getTitleUrl();
    }
    String Es(Object[] ignored) {
        return getDescriptionUrl();
    }
    String EB(Object[] ignored) {
        return getAuthorUrl();
    }
}
