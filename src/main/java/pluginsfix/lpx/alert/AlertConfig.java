package pluginsfix.lpx.alert;
import pluginsfix.lpx.api.LPXPlugin;
import pluginsfix.lpx.config.ConfigSection;
import pluginsfix.lpx.config.MessagesConfig;
import java.util.List;
public class AlertConfig extends ConfigSection {
    private final String permission;
    private final String format;
    private final List<String> hoverLines;
    private final String enabledMessage;
    private final String disabledMessage;
    public AlertConfig(char ignoredA, int ignoredB, LPXPlugin plugin, MessagesConfig messages, short ignoredC, String basePath) {
        super(plugin, basePath + ".alerts", messages);
        this.permission = lp(new Object[]{0L, "permission"});
        this.format = lp(new Object[]{0L, "format"});
        this.hoverLines = lt(new Object[]{0L, "hover"});
        this.enabledMessage = lp(new Object[]{0L, "enabled"});
        this.disabledMessage = lp(new Object[]{0L, "disabled"});
    }
    public String getPermission() {
        return permission;
    }
    public String getFormat() {
        return format;
    }
    public List<String> getHoverLines() {
        return hoverLines;
    }
    public String getEnabledMessage() {
        return enabledMessage;
    }
    public String getDisabledMessage() {
        return disabledMessage;
    }
    public String nl(Object[] ignored) {
        return getPermission();
    }
    public String nd(Object[] ignored) {
        return getFormat();
    }
    @SuppressWarnings("rawtypes")
    public List nt(Object[] ignored) {
        return getHoverLines();
    }
    public String np(Object[] ignored) {
        return getEnabledMessage();
    }
    public String nh(Object[] ignored) {
        return getDisabledMessage();
    }
}
