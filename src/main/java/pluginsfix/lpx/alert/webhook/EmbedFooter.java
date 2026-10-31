package pluginsfix.lpx.alert.webhook;
class EmbedFooter {
    private final String text;
    EmbedFooter(String text) {
        this.text = text;
    }
    String getText() {
        return text;
    }
    String Gz(Object[] ignored) {
        return getText();
    }
}
