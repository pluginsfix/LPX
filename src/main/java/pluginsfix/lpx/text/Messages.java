package pluginsfix.lpx.text;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class Messages {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private final File file;
    private YamlConfiguration config;

    public Messages(File file) {
        this.file = file;
        reload();
    }

    public synchronized void reload() {
        if (!file.exists()) {
            this.config = new YamlConfiguration();
            return;
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void send(CommandSender sender, String key, Placeholder... placeholders) {
        if (config == null || sender == null) {
            return;
        }

        List<String> lines = getRawList(key);
        if (lines.isEmpty()) {
            return;
        }

        Player player = sender instanceof Player p ? p : null;

        for (String line : lines) {
            String processed = applyPlaceholders(line, placeholders);

            if (processed.startsWith("[message] ")) {
                String msg = colorize(processed.substring(10));
                sender.sendMessage(msg);
            } else if (processed.startsWith("[sound] ")) {
                if (player != null) {
                    playSound(player, processed.substring(8).trim());
                }
            } else if (processed.startsWith("[actionbar] ")) {
                if (player != null) {
                    sendActionBar(player, colorize(processed.substring(12)));
                }
            } else {
                sender.sendMessage(colorize(processed));
            }
        }
    }

    public String get(String key, Placeholder... placeholders) {
        if (config == null) {
            return "";
        }
        if (config.isList(key)) {
            List<String> list = config.getStringList(key);
            for (String line : list) {
                if (line.startsWith("[message] ")) {
                    return colorize(applyPlaceholders(line.substring(10), placeholders));
                }
            }
            if (!list.isEmpty()) {
                return colorize(applyPlaceholders(list.get(0), placeholders));
            }
            return "";
        }
        String single = config.getString(key, "");
        if (single.startsWith("[message] ")) {
            single = single.substring(10);
        }
        return colorize(applyPlaceholders(single, placeholders));
    }

    public List<String> getList(String key, Placeholder... placeholders) {
        if (config == null) {
            return Collections.emptyList();
        }
        List<String> raw = getRawList(key);
        List<String> result = new ArrayList<>(raw.size());
        for (String line : raw) {
            if (line.startsWith("[message] ")) {
                line = line.substring(10);
            }
            result.add(colorize(applyPlaceholders(line, placeholders)));
        }
        return result;
    }

    private List<String> getRawList(String key) {
        if (config.isList(key)) {
            return config.getStringList(key);
        }
        String single = config.getString(key);
        if (single != null && !single.isEmpty()) {
            return Collections.singletonList(single);
        }
        return Collections.emptyList();
    }

    private String applyPlaceholders(String text, Placeholder... placeholders) {
        if (text == null || placeholders == null) {
            return text == null ? "" : text;
        }
        for (Placeholder placeholder : placeholders) {
            if (placeholder != null && placeholder.key() != null) {
                text = text.replace("{" + placeholder.key() + "}", placeholder.value());
                text = text.replace("%" + placeholder.key() + "%", placeholder.value());
            }
        }
        return text;
    }

    private String colorize(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    private void playSound(Player player, String soundName) {
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException ignored) {
        }
    }

    private void sendActionBar(Player player, String message) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        } catch (Throwable ignored) {
            player.sendMessage(message);
        }
    }
}
