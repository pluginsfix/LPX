package pluginsfix.lpx.config;
import pluginsfix.lpx.api.LPXPlugin;
public class ThresholdsConfig extends ConfigSection {
    private final long w;
    private final long O;
    private final long v;
    public ThresholdsConfig(LPXPlugin plugin, long ignored, String basePath, MessagesConfig messagesConfig) {
        super(plugin, basePath + ".thresholds", messagesConfig);
        this.w = readLong("warn-delay-ms", "warn-delay", 750L);
        this.O = readLong("check-interval-ms", "check-interval", 1000L);
        this.v = readLong("critical-delay-ms", "critical-delay", 3000L);
    }
    public long ve(Object[] args) {
        return w;
    }
    public long vq(Object[] args) {
        return O;
    }
    public long va(Object[] args) {
        return v;
    }
    private long readLong(String primary, String legacy, long fallback) {
        String primaryPath = i + "." + primary;
        if (sl(new Object[0]).contains(primaryPath)) {
            return sl(new Object[0]).getLong(primaryPath, fallback);
        }
        return s1(new Object[]{legacy, fallback});
    }
}
