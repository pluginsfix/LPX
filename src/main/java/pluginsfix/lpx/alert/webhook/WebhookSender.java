package pluginsfix.lpx.alert.webhook;

import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.config.UpdateConfig;
import pluginsfix.lpx.core.LPXCore;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class WebhookSender {
    private static final Pattern WEBHOOK_URL_PATTERN =
        Pattern.compile("^https://(?:(?:canary|ptb)\\.)?discord(?:app)?\\.com/api/webhooks/\\d+/[A-Za-z0-9_-]+$");
    private final LPXCore core;
    private Color embedColor = Color.CYAN;
    private String webhookUrl;

    public WebhookSender(LPXCore core) {
        this.core = core;
    }

    public void wh(Object[] ignored) {
        UpdateConfig config = core.sZ(new Object[0]).BY(new Object[0]);
        if (!config.p_(new Object[0])) {
            return;
        }
        String url = config.pY(new Object[0]);
        if (url == null || url.isBlank()) {
            return;
        }
        if (!WEBHOOK_URL_PATTERN.matcher(url).matches()) {
            throw new IllegalArgumentException("Invalid Discord webhook URL: " + url);
        }
        this.embedColor = parseColor(config.po(new Object[0]));
        this.webhookUrl = url;
    }

    public void AJ(Object... args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        Check<?> check = (Check<?>) args[1];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        if (webhookUrl == null || webhookUrl.isBlank()) {
            return;
        }
        try {
            String messageTemplate = buildMessageTemplate();
            String alertText = core.s1(new Object[0]).n5(new Object[]{messageTemplate, 0L, player, check, infos});
            String sanitized = stripColorCodes(alertText).replace("_", "\\_");
            EmbedField embed = new EmbedField()
                .qG(new Object[]{"LPX Alert"})
                .qv(new Object[]{"https://minotar.net/avatar/" + player.getUsername() + "/100.png"})
                .ml(new Object[]{embedColor})
                .ql(new Object[]{"Player: " + player.getUsername()})
                .q7(new Object[]{0L, sanitized})
                ._J(new Object[]{Instant.now()});
            WebhookPayload payload = new WebhookPayload(webhookUrl);
            payload.PG(new Object[]{embed});
            payload.a9(new Object[]{0L});
        } catch (IOException exception) {
            core.getLogger().log(Level.WARNING, "Failed to send webhook payload: " + exception.getMessage(), exception);
        } catch (IllegalArgumentException exception) {
            core.getLogger().log(Level.WARNING, "Webhook payload rejected: " + exception.getMessage(), exception);
        } catch (Exception exception) {
            core.getLogger().log(Level.WARNING, "Unexpected webhook sender error: " + exception.getMessage(), exception);
        }
    }

    private String buildMessageTemplate() {
        List<?> lines = core.sZ(new Object[0]).BY(new Object[0]).pq(new Object[0]);
        StringBuilder builder = new StringBuilder();
        for (Object line : lines) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append((String) line);
        }
        return builder.toString();
    }

    private Color parseColor(String raw) {
        try {
            return Color.decode(raw);
        } catch (Exception ignored) {
            return Color.CYAN;
        }
    }

    private String stripColorCodes(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("(?i)[&\u00A7][0-9A-FK-ORX]", "");
    }
}
