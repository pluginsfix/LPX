package pluginsfix.lpx.config;

import pluginsfix.lpx.api.LPXPlugin;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigSection {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[A-Fa-f0-9]{6}");

    protected final LPXPlugin x;
    protected final String i;
    @Nullable
    private MessagesConfig Q;
    private static PacketWrapper[] w;

    public ConfigSection(LPXPlugin plugin, String sectionPath, @Nullable MessagesConfig messagesConfig) {
        this.x = plugin;
        this.i = sectionPath;
        this.Q = messagesConfig;
    }

    public void setMessagesConfig(@Nullable MessagesConfig messagesConfig) {
        this.Q = messagesConfig;
    }

    public FileConfiguration getConfig() {
        return x.getConfig();
    }

    protected String getString(String key) {
        String value = getConfig().getString(i + "." + key);
        return translateWithPlaceholders(value);
    }

    protected boolean getBoolean(String key) {
        return getConfig().getBoolean(i + "." + key);
    }

    protected boolean getBoolean(String key, boolean defaultValue) {
        return getConfig().getBoolean(i + "." + key, defaultValue);
    }

    protected long getLong(String key, long defaultValue) {
        return getConfig().getLong(i + "." + key, defaultValue);
    }

    protected int getInt(String key) {
        return getConfig().getInt(i + "." + key);
    }

    protected List<String> getStringList(String key) {
        List<String> rawList = getConfig().getStringList(i + "." + key);
        List<String> translated = new ArrayList<>(rawList.size());
        for (String line : rawList) {
            translated.add(translateWithPlaceholders(line));
        }
        return translated;
    }

    private String translateWithPlaceholders(String value) {
        if (value == null) {
            return "";
        }

        MessagesConfig messagesConfig = this.Q;
        if (messagesConfig != null) {
            value = value.replace("%main-color%", messagesConfig.Vj(new Object[0]));
            value = value.replace("%second-color%", messagesConfig.Vp(new Object[0]));
            String prefix = messagesConfig.V8(new Object[0]);
            if (prefix != null) {
                value = value.replace("%prefix%", prefix);
            }
        }

        return translateColors(value);
    }

    private String translateColors(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        boolean supportsHex = false;
        try {
            supportsHex = PacketEvents.getAPI()
                .getServerManager()
                .getVersion()
                .is(VersionComparison.NEWER_THAN, ServerVersion.V_1_15_2);
        } catch (Throwable ignored) {
        }

        if (supportsHex) {
            Matcher matcher = Pattern.compile("&#([A-Fa-f0-9]{6})").matcher(input);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String hex = matcher.group(1);
                matcher.appendReplacement(sb, net.md_5.bungee.api.ChatColor.of("#" + hex).toString());
            }
            matcher.appendTail(sb);
            input = sb.toString();
        }

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void setLegacyState(PacketWrapper[] packetWrapperArray) {
        w = packetWrapperArray;
    }

    public static PacketWrapper[] getLegacyState() {
        return w;
    }

    public void Yv(@Nullable MessagesConfig messagesConfig) {
        setMessagesConfig(messagesConfig);
    }

    public FileConfiguration sl(Object[] ignored) {
        return getConfig();
    }

    protected String lp(Object[] args) {
        return getString((String) args[1]);
    }

    protected boolean ln(Object[] args) {
        return getBoolean((String) args[0]);
    }

    protected boolean GE(Object[] args) {
        return getBoolean((String) args[0], (Boolean) args[1]);
    }

    protected long s1(Object[] args) {
        return getLong((String) args[0], (Long) args[1]);
    }

    protected int l6(Object[] args) {
        return getInt((String) args[0]);
    }

    protected List<String> lt(Object[] args) {
        return getStringList((String) args[1]);
    }

    public static void lm(PacketWrapper[] packetWrapperArray) {
        setLegacyState(packetWrapperArray);
    }

    public static PacketWrapper[] sH() {
        return getLegacyState();
    }
}
