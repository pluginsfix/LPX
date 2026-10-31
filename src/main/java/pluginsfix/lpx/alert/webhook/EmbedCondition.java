package pluginsfix.lpx.alert.webhook;
class EmbedCondition {
    private final String key;
    private final String expectedValue;
    private final boolean equals;
    EmbedCondition(String key, String expectedValue, boolean equals) {
        this.key = key;
        this.expectedValue = expectedValue;
        this.equals = equals;
    }
    String getKey() {
        return key;
    }
    String getExpectedValue() {
        return expectedValue;
    }
    boolean isEquals() {
        return equals;
    }
    String Zr(Object[] ignored) {
        return getKey();
    }
    String Z9(Object[] ignored) {
        return getExpectedValue();
    }
    boolean Zu(Object[] ignored) {
        return isEquals();
    }
}
