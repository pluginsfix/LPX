package pluginsfix.lpx.check.impl.command;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
public class CommandCheckBlacklist extends AbstractCheckT<PlayerCommandPreprocessEvent> {
    private List<String> blockedCommands = new ArrayList<>();
    private static int[] legacyState;
    public CommandCheckBlacklist(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        List<String> values = j_(new Object[0]).getStringList(kD(new Object[]{"blacklist", 0L}));
        if (values.isEmpty()) {
            values = j_(new Object[0]).getStringList(kD(new Object[]{"commands", 0L}));
        }
        blockedCommands = new ArrayList<>();
        for (String value : values) {
            if (value == null) {
                continue;
            }
            String normalized = value.trim().toLowerCase(Locale.ROOT);
            if (!normalized.isEmpty()) {
                blockedCommands.add(normalized);
            }
        }
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return AP(args);
    }
    protected CheckResult AP(Object[] args) {
        PlayerCommandPreprocessEvent event = (PlayerCommandPreprocessEvent) args[2];
        String message = event.getMessage();
        if (message == null || message.isEmpty()) {
            return CheckResult.ALLOW;
        }
        String normalizedMessage = message.trim().toLowerCase(Locale.ROOT);
        for (String blocked : blockedCommands) {
            if (matchesCommand(normalizedMessage, blocked)) {
                AZ(new Object[]{event, 0L, new FlagInfo[]{FlagInfo.of("command", message)}});
                return CheckResult.DENY;
            }
        }
        return CheckResult.ALLOW;
    }
    private boolean matchesCommand(String fullMessage, String blocked) {
        if (blocked.isEmpty()) {
            return false;
        }
        String normalizedBlocked = blocked.startsWith("/") ? blocked : "/" + blocked;
        return fullMessage.equals(normalizedBlocked)
            || fullMessage.startsWith(normalizedBlocked + " ")
            || fullMessage.equals(blocked)
            || fullMessage.startsWith(blocked + " ");
    }
    public static void hS(int[] value) {
        legacyState = value;
    }
    public static int[] xW() {
        return legacyState;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
