package pluginsfix.lpx.api.check.options;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.LPXPlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
public class CheckOptions {
    private final LPXPlugin plugin;
    private final String path;
    private final CheckType type;
    private boolean enabled;
    private boolean punish;
    private int maxVL;
    private int minVl;
    private List<String> punishCommands;
    private int maxBuffer;
    private int bufferMultiplier;
    private int bufferDecay;
    private static boolean legacyState;
    public CheckOptions(LPXPlugin plugin, CheckType type) {
        this.plugin = plugin;
        this.type = type;
        this.path = "checks."
            + type.getCategory().name().toLowerCase()
            + "."
            + type.getId().name().toLowerCase()
            + ".";
        reload();
    }
    public void reload() {
        FileConfiguration config = getConfig();
        enabled = config.getBoolean(path + "enabled", true);
        punish = config.getBoolean(path + "punish");
        maxVL = config.getInt(path + "max-vl");
        minVl = config.getInt(path + "min-vl");
        punishCommands = new ArrayList<>(config.getStringList(path + "punish-commands"));
        for (int i = 0; i < punishCommands.size(); i++) {
            punishCommands.set(i, ChatColor.translateAlternateColorCodes('&', punishCommands.get(i)));
        }
        maxBuffer = config.getInt(path + "buffer.max", 1);
        bufferMultiplier = config.getInt(path + "buffer.multiply", 0);
        bufferDecay = config.getInt(path + "buffer.decay", 1);
    }
    public LPXPlugin getPlugin() {
        return plugin;
    }
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public String getPath() {
        return path;
    }
    public CheckType getType() {
        return type;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isPunish() {
        return punish;
    }
    public int getMaxVL() {
        return maxVL;
    }
    public int getMinVl() {
        return minVl;
    }
    public List<String> getPunishCommands() {
        return punishCommands;
    }
    public int getMaxBuffer() {
        return maxBuffer;
    }
    public int getBufferMultiplier() {
        return bufferMultiplier;
    }
    public int getBufferDecay() {
        return bufferDecay;
    }
    public static void TH(boolean value) {
        legacyState = value;
    }
    public static boolean VJ() {
        return legacyState;
    }
    public static boolean VH() {
        return !legacyState;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
