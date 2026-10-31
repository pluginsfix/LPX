package pluginsfix.lpx.alert.webhook;
import pluginsfix.lpx.api.LPX;
import java.awt.Color;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
public class WebhookPayload {
    private static final String CONTENT_TYPE = "application/json";
    private static final String USER_AGENT = "LPX-Webhook/1.0";
    private final String webhookUrl;
    private final List<EmbedField> embeds = new ArrayList<>();
    private String username;
    private String avatarUrl;
    private String content;
    private boolean tts;
    private String rawPayload;
    private static boolean debugFlag;
    public WebhookPayload(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
    public void YR(Object[] args) {
        this.username = (String) args[0];
    }
    public void Y1(Object[] args) {
        this.avatarUrl = (String) args[0];
    }
    public void Y2(Object[] args) {
        this.content = (String) args[0];
    }
    public void bD(Object[] args) {
        this.tts = (Boolean) args[0];
    }
    public void PG(Object[] args) {
        this.embeds.add((EmbedField) args[0]);
    }
    public void a9(Object[] ignored) throws IOException {
        if ((content == null || content.isBlank()) && embeds.isEmpty()) {
            throw new IllegalArgumentException("Webhook payload must contain content or at least one embed.");
        }
        String payload = toJson();
        this.rawPayload = payload;
        HttpsURLConnection connection = (HttpsURLConnection) new URL(webhookUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", CONTENT_TYPE);
        connection.addRequestProperty("Accept", CONTENT_TYPE);
        connection.addRequestProperty("User-Agent", USER_AGENT);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
        }
        int code = connection.getResponseCode();
        if (code >= 400) {
            String error = readStream(connection.getErrorStream());
            connection.disconnect();
            throw new IOException("Webhook request failed with HTTP " + code + (error.isBlank() ? "" : ": " + error));
        }
        try (InputStream inputStream = connection.getInputStream()) {
            while (inputStream.read() != -1) {
            }
        }
        connection.disconnect();
    }
    public String a1(Object[] ignored) {
        return rawPayload;
    }
    public static void bK(boolean value) {
        debugFlag = value;
    }
    public static boolean av() {
        return debugFlag;
    }
    public static boolean aJ() {
        return !debugFlag;
    }
    private String toJson() {
        StringBuilder json = new StringBuilder("{");
        boolean[] first = new boolean[]{true};
        appendStringField(json, first, "username", username);
        appendStringField(json, first, "avatar_url", avatarUrl);
        appendStringField(json, first, "content", content);
        appendBooleanField(json, first, "tts", tts);
        if (!embeds.isEmpty()) {
            appendComma(json, first);
            json.append("\"embeds\":[");
            for (int i = 0; i < embeds.size(); i++) {
                json.append(toJson(embeds.get(i)));
                if (i + 1 < embeds.size()) {
                    json.append(',');
                }
            }
            json.append(']');
        }
        json.append('}');
        return json.toString();
    }
    private String toJson(EmbedField embed) {
        StringBuilder json = new StringBuilder("{");
        boolean[] first = new boolean[]{true};
        appendStringField(json, first, "title", embed.Qk(new Object[0]));
        appendStringField(json, first, "description", embed.Q1(new Object[0]));
        appendStringField(json, first, "url", embed.QX(new Object[0]));
        appendStringField(json, first, "timestamp", embed.QM(new Object[0]));
        Color color = embed.Qm(new Object[0]);
        if (color != null) {
            int colorValue = (color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue();
            appendNumberField(json, first, "color", colorValue);
        }
        WebhookAuth author = embed.Q_(new Object[0]);
        if (author != null) {
            appendComma(json, first);
            json.append("\"author\":{");
            boolean[] firstAuthorField = new boolean[]{true};
            appendStringField(json, firstAuthorField, "name", author.sm(new Object[0]));
            appendStringField(json, firstAuthorField, "icon_url", author.s6(new Object[0]));
            json.append('}');
        }
        EmbedFooter footer = embed.Qb(new Object[0]);
        if (footer != null) {
            appendComma(json, first);
            json.append("\"footer\":{");
            boolean[] firstFooterField = new boolean[]{true};
            appendStringField(json, firstFooterField, "text", footer.Gz(new Object[0]));
            json.append('}');
        }
        EmbedThumbnail thumbnail = embed.QK(new Object[0]);
        if (thumbnail != null) {
            appendComma(json, first);
            json.append("\"thumbnail\":{");
            boolean[] firstThumbField = new boolean[]{true};
            appendStringField(json, firstThumbField, "url", thumbnail.pv(new Object[0]));
            json.append('}');
        }
        FieldUrls urls = embed.Qy(new Object[0]);
        if (urls != null) {
            appendComma(json, first);
            json.append("\"urls\":{");
            boolean[] firstUrlField = new boolean[]{true};
            appendStringField(json, firstUrlField, "title", urls.EC(new Object[0]));
            appendStringField(json, firstUrlField, "description", urls.Es(new Object[0]));
            appendStringField(json, firstUrlField, "author", urls.EB(new Object[0]));
            json.append('}');
        }
        @SuppressWarnings("unchecked")
        List<EmbedCondition> conditions = (List<EmbedCondition>) embed.QB(new Object[0]);
        if (conditions != null && !conditions.isEmpty()) {
            appendComma(json, first);
            json.append("\"conditions\":[");
            for (int i = 0; i < conditions.size(); i++) {
                EmbedCondition condition = conditions.get(i);
                json.append('{');
                boolean[] firstConditionField = new boolean[]{true};
                appendStringField(json, firstConditionField, "key", condition.Zr(new Object[0]));
                appendStringField(json, firstConditionField, "value", condition.Z9(new Object[0]));
                appendBooleanField(json, firstConditionField, "equals", condition.Zu(new Object[0]));
                json.append('}');
                if (i + 1 < conditions.size()) {
                    json.append(',');
                }
            }
            json.append(']');
        }
        json.append('}');
        return json.toString();
    }
    private static void appendStringField(StringBuilder json, boolean[] first, String key, String value) {
        if (value == null) {
            return;
        }
        appendComma(json, first);
        json.append('"').append(escape(key)).append('"').append(':').append('"').append(escape(value)).append('"');
    }
    private static void appendBooleanField(StringBuilder json, boolean[] first, String key, boolean value) {
        appendComma(json, first);
        json.append('"').append(escape(key)).append('"').append(':').append(value);
    }
    private static void appendNumberField(StringBuilder json, boolean[] first, String key, int value) {
        appendComma(json, first);
        json.append('"').append(escape(key)).append('"').append(':').append(value);
    }
    private static void appendComma(StringBuilder json, boolean[] first) {
        if (!first[0]) {
            json.append(',');
        } else {
            first[0] = false;
        }
    }
    private static String readStream(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
    private static String escape(String text) {
        StringBuilder escaped = new StringBuilder(text.length() + 8);
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (ch < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                    break;
            }
        }
        return escaped.toString();
    }
}
