package pluginsfix.lpx.config;
import pluginsfix.lpx.alert.LogTarget;
import pluginsfix.lpx.api.LPXPlugin;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
public class PunishConfig extends ConfigSection {
    private static final String BASE_PATH = "packet-logger";
    private static String debugToken;
    private final long heavyPacketThreshold;
    private final Set<String> heavyIgnoredPackets;
    private final boolean enabled;
    private final LogTarget logTarget;
    private final ThresholdsConfig thresholds;
    private final FilterMode playerMode;
    private final List<String> players;
    private final FilterMode packetMode;
    private final List<String> packets;
    public PunishConfig(long ignored, MessagesConfig messagesConfig, LPXPlugin plugin) {
        super(plugin, BASE_PATH, messagesConfig);
        this.heavyPacketThreshold = s1(new Object[]{"heavy-packet-threshold", 5_000_000L});
        this.heavyIgnoredPackets = new HashSet<>(
            lt(new Object[]{0L, "heavy-ignored-packets"})
                .stream()
                .map(value -> ((String) value).toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet())
        );
        this.enabled = ln(new Object[]{"enabled"});
        this.logTarget = parseLogTarget(lp(new Object[]{0L, "output"}));
        this.thresholds = new ThresholdsConfig(plugin, 0L, BASE_PATH + ".netty-watchdog", messagesConfig);
        this.playerMode = parseFilterMode(lp(new Object[]{0L, "player-mode"}));
        this.players = lt(new Object[]{0L, "players"});
        this.packetMode = parseFilterMode(lp(new Object[]{0L, "packet-mode"}));
        this.packets = lt(new Object[]{0L, "packets"});
    }
    private FilterMode parseFilterMode(String raw) {
        if (raw == null || raw.isBlank()) {
            return FilterMode.WHITELIST;
        }
        try {
            return FilterMode.valueOf(raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            x.getLogger().warning("Unknown filter mode '" + raw + "', falling back to WHITELIST.");
            return FilterMode.WHITELIST;
        }
    }
    private LogTarget parseLogTarget(String raw) {
        if (raw == null || raw.isBlank()) {
            return LogTarget.FILE;
        }
        try {
            return LogTarget.valueOf(raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            x.getLogger().warning("Unknown log target '" + raw + "', falling back to FILE.");
            return LogTarget.FILE;
        }
    }
    public boolean Jq(Object[] args) {
        String playerName = (String) args[1];
        if (playerMode == FilterMode.WHITELIST) {
            return players.contains(playerName);
        }
        return !players.contains(playerName);
    }
    public boolean Jn(Object[] args) {
        String packetName = (String) args[1];
        if (packetMode == FilterMode.WHITELIST) {
            return packets.contains(packetName);
        }
        return !packets.contains(packetName);
    }
    public boolean Zv(Object[] args) {
        long elapsedNs = (Long) args[3];
        if (heavyPacketThreshold <= 0L) {
            return false;
        }
        return elapsedNs >= heavyPacketThreshold;
    }
    public boolean Jt(Object[] args) {
        String packetName = (String) args[0];
        return heavyIgnoredPackets.contains(packetName.toUpperCase(Locale.ROOT));
    }
    public boolean Xu(Object[] ignored) {
        return enabled;
    }
    public ThresholdsConfig Xi(Object[] ignored) {
        return thresholds;
    }
    public LogTarget XJ(Object[] ignored) {
        return logTarget;
    }
    public static void Jp(String value) {
        debugToken = value;
    }
    public static String X0() {
        return debugToken;
    }
}
