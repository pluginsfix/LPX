package pluginsfix.lpx.text;

public record Placeholder(String key, String value) {
    public static Placeholder of(String key, Object value) {
        return new Placeholder(key, value == null ? "" : String.valueOf(value));
    }
}
