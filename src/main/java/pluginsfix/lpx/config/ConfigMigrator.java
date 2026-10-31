package pluginsfix.lpx.config;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.api.LPXPlugin;
import java.io.InputStreamReader;
import java.io.Reader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
public final class ConfigMigrator {
    private ConfigMigrator() {
    }
    public static void migrate(LPXPlugin plugin) {
        try {
            if (!plugin.getConfig().isList("checks")) {
                return;
            }
            try (Reader reader = new InputStreamReader(plugin.getResource("config.yml"))) {
                if (reader == null) {
                    plugin.getLogger().warning("LPX: cannot migrate config, default config.yml not found in jar.");
                    return;
                }
                YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
                ConfigurationSection checksDefaults = defaults.getConfigurationSection("checks");
                if (checksDefaults == null) {
                    plugin.getLogger().warning("LPX: cannot migrate config, default checks section not found.");
                    return;
                }
                plugin.getConfig().set("checks", checksDefaults.getValues(true));
                plugin.saveConfig();
                plugin.getLogger().info("LPX: migrated legacy checks list to checks section format.");
            }
        } catch (Throwable throwable) {
            plugin.getLogger().warning("LPX: config migration failed: " + throwable.getMessage());
        }
    }
    public static void d5(Object[] args) {
        LPXPlugin plugin = (LPXPlugin) args[0];
        migrate(plugin);
    }
}
