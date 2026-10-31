package pluginsfix.lpx.alert.webhook;
class WebhookAuth {
    private final String username;
    private final String avatarUrl;
    WebhookAuth(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }
    String getUsername() {
        return username;
    }
    String getAvatarUrl() {
        return avatarUrl;
    }
    String sm(Object[] ignored) {
        return getUsername();
    }
    String s6(Object[] ignored) {
        return getAvatarUrl();
    }
}
