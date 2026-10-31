package pluginsfix.lpx;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pluginsfix.lpx.api.LPXPlugin;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.text.Messages;

public class LPX extends JavaPlugin implements LPXPlugin {
    private static LPX instance;
    private pluginsfix.lpx.api.LPX core;
    private FileConfiguration configuration;
    private File configFile;
    private Messages messages;

    public static LPX getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI()
            .getSettings()
            .reEncodeByDefault(false)
            .checkForUpdates(false)
            .fullStackTrace(true)
            .kickOnPacketException(true);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            initMessages();
            reload();
            this.core = new LPXCore(this, 0L);
        } catch (Throwable throwable) {
            getLogger().severe("LPX failed to enable.");
            throwable.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            if (core != null) {
                core.disable(true);
            } else {
                PacketEvents.getAPI().terminate();
            }
        } catch (Throwable ignored) {
        }
    }

    private void initMessages() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        this.messages = new Messages(messagesFile);
    }

    public Messages getMessages() {
        if (messages == null) {
            initMessages();
        }
        return messages;
    }

    public pluginsfix.lpx.api.LPX getCore() {
        return core;
    }

    @Override
    public File getConfigFile() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        return configFile;
    }

    @Override
    public FileConfiguration getConfig() {
        if (configuration == null) {
            configuration = super.getConfig();
        }
        return configuration;
    }

    @Override
    public void saveConfig() {
        if (configuration == null) {
            super.saveConfig();
            return;
        }
        File file = getConfigFile();
        try {
            configuration.save(file);
        } catch (IOException exception) {
            getLogger().warning("Unable to save config.yml: " + exception.getMessage());
        }
    }

    @Override
    public void reload() {
        super.reloadConfig();
        this.configuration = super.getConfig();
        this.configFile = getConfigFile();
        if (messages != null) {
            messages.reload();
        }
    }
}
