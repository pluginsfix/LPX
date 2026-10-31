package pluginsfix.lpx.alert.webhook;
import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;
class JsonBuilder {
    private final Map<String, Object> values = new LinkedHashMap<>();
    JsonBuilder() {
    }
    void Hi(Object[] args) {
        String key = (String) args[0];
        Object value = args[1];
        if (value == null) {
            return;
        }
        values.put(key, value);
    }
    @Override
    public String toString() {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!first) {
                json.append(',');
            }
            first = false;
            json.append(VK(new Object[]{entry.getKey()})).append(':').append(formatValue(entry.getValue()));
        }
        json.append('}');
        return json.toString();
    }
    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof JsonBuilder) {
            return value.toString();
        }
        if (value.getClass().isArray()) {
            StringBuilder builder = new StringBuilder("[");
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    builder.append(',');
                }
                builder.append(formatValue(Array.get(value, i)));
            }
            return builder.append(']').toString();
        }
        return VK(new Object[]{String.valueOf(value)});
    }
    private String VK(Object[] args) {
        String value = (String) args[0];
        return "\"" + escape(value) + "\"";
    }
    private String escape(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\b", "\\b")
            .replace("\f", "\\f")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
