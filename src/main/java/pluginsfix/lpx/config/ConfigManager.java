package pluginsfix.lpx.config;

import pluginsfix.lpx.api.LPXPlugin;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.CheckContainer;
import pluginsfix.lpx.core.LPXCore;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private MessagesConfig messagesConfig;
    private PacketEventsConfig packetEventsConfig;
    private UpdateConfig updateConfig;
    private PunishConfig punishConfig;
    private static int legacyState;

    public ConfigManager(long ignored, LPXPlugin plugin) {
        plugin.saveDefaultConfig();
        migrateLegacyChecksSection(plugin);
        ConfigMigrator.d5(new Object[]{plugin, 0L});
    }

    public void load(LPXPlugin plugin) {
        this.messagesConfig = new MessagesConfig(plugin, 0L);
        this.packetEventsConfig = new PacketEventsConfig(messagesConfig, plugin, 0L);
        this.punishConfig = new PunishConfig(0L, messagesConfig, plugin);
        this.updateConfig = new UpdateConfig(messagesConfig, plugin, 0L);
    }

    private void migrateLegacyChecksSection(LPXPlugin plugin) {
        if (!plugin.getConfig().isList("checks")) {
            return;
        }

        try (Reader reader = new InputStreamReader(plugin.getResource("config.yml"))) {
            if (reader == null) {
                return;
            }
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
            ConfigurationSection checksSection = defaults.getConfigurationSection("checks");
            if (checksSection == null) {
                return;
            }

            plugin.getConfig().set("checks", checksSection.getValues(true));
            plugin.saveConfig();
            plugin.getLogger().info("LPX: migrated legacy checks list to checks section.");
        } catch (Exception exception) {
            plugin.getLogger().warning("LPX: unable to migrate legacy checks section: " + exception.getMessage());
        }
    }

    public void reload(LPXCore core) {
        core.getPlugin().reload();
        load(core.getPlugin());

        for (Collection<?> registeredChecks : core.getCheckManager().getChecks().values()) {
            for (Object entry : registeredChecks) {
                if (entry instanceof pluginsfix.lpx.api.check.RegisteredCheck<?> registeredCheck) {
                    registeredCheck.getOptions().reload();
                }
            }
        }

        for (ProtocolPlayer player : core.getRegistry().getPlayers().values()) {
            for (CheckContainer<?> container : player.getChecks().values()) {
                for (Check<?> check : container) {
                    check.reload();
                }
            }
        }

        core.s1(new Object[0]).flushWebhook();
    }

    public PacketEventsConfig getPacketEventsConfig() {
        return packetEventsConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public PunishConfig getPunishConfig() {
        return punishConfig;
    }

    public UpdateConfig getUpdateConfig() {
        return updateConfig;
    }

    public static void setLegacyState(int value) {
        legacyState = value;
    }

    public static int getLegacyState() {
        return legacyState;
    }

    public static int getLegacyFallbackValue() {
        return getLegacyState() == 0 ? 42 : 0;
    }

    public void nY(Object[] args) {
        load((LPXPlugin) args[0]);
    }

    private void nB(Object[] args) {
        migrateLegacyChecksSection((LPXPlugin) args[1]);
    }

    public void cF(Object[] args) {
        reload((LPXCore) args[0]);
    }

    public PacketEventsConfig Bw(Object[] ignored) {
        return getPacketEventsConfig();
    }

    public MessagesConfig BM(Object[] ignored) {
        return getMessagesConfig();
    }

    public PunishConfig BJ(Object[] ignored) {
        return getPunishConfig();
    }

    public UpdateConfig BY(Object[] ignored) {
        return getUpdateConfig();
    }

    public static void dr(int value) {
        setLegacyState(value);
    }

    public static int BG() {
        return getLegacyState();
    }

    public static int Bo() {
        return getLegacyFallbackValue();
    }
}
