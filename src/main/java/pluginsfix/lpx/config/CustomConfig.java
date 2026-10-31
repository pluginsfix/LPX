package pluginsfix.lpx.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomConfig {
    private final File file;
    private final String fileName;
    private FileConfiguration configuration;

    public CustomConfig(JavaPlugin plugin, String fileName, boolean externalConfig, long ignored) {
        String normalizedName = fileName.endsWith(".yml") ? fileName : fileName + ".yml";
        this.fileName = normalizedName;

        if (externalConfig) {
            File worldContainer = Bukkit.getServer().getWorldContainer().getAbsoluteFile();
            File parent = worldContainer.getParentFile() == null ? worldContainer : worldContainer.getParentFile();
            File parentParent = parent.getParentFile() == null ? parent : parent.getParentFile();
            this.file = new File(new File(parentParent, "config"), normalizedName);
        } else {
            this.file = new File(plugin.getDataFolder(), normalizedName);
        }

        load(plugin);
    }

    private void load(JavaPlugin plugin) {
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                plugin.saveResource(fileName, false);
            }

            this.configuration = new YamlConfiguration();
            this.configuration.load(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public int getInt(String path) {
        return configuration.getInt(path);
    }

    public int getInt(String path, int defaultValue) {
        return configuration.getInt(path, defaultValue);
    }

    public String getString(String path) {
        return configuration.getString(path);
    }

    public String getString(String path, String defaultValue) {
        return configuration.getString(path, defaultValue);
    }

    public double getDouble(String path) {
        return configuration.getDouble(path);
    }

    public double getDouble(String path, double defaultValue) {
        return configuration.getDouble(path, defaultValue);
    }

    public boolean getBoolean(String path) {
        return configuration.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return configuration.getBoolean(path, defaultValue);
    }

    public long getLong(String path) {
        return configuration.getLong(path);
    }

    public long getLong(String path, long defaultValue) {
        return configuration.getLong(path, defaultValue);
    }

    public List<?> getList(String path) {
        return configuration.getList(path);
    }

    public List<?> getList(String path, List<?> defaultValue) {
        return configuration.getList(path, defaultValue);
    }

    public List<String> getStringList(String path) {
        return configuration.getStringList(path);
    }

    public ConfigurationSection getSection(String path) {
        return configuration.getConfigurationSection(path);
    }

    public void hH(Object[] args) {
        load((JavaPlugin) args[1]);
    }

    public void jG(Object[] ignored) {
        save();
    }

    public File jU(Object[] ignored) {
        return getFile();
    }

    public FileConfiguration jr(Object[] ignored) {
        return getConfiguration();
    }

    public int Jo(Object[] args) {
        return getInt((String) args[0]);
    }

    public int VH(Object[] args) {
        return getInt((String) args[0], (Integer) args[1]);
    }

    public String Je(Object[] args) {
        return getString((String) args[0]);
    }

    public String on(Object[] args) {
        return getString((String) args[0], (String) args[1]);
    }

    public double Js(Object[] args) {
        return getDouble((String) args[0]);
    }

    public double LS(Object[] args) {
        return getDouble((String) args[0], (Double) args[1]);
    }

    public boolean Jg(Object[] args) {
        return getBoolean((String) args[0]);
    }

    public boolean GG(Object[] args) {
        return getBoolean((String) args[0], (Boolean) args[1]);
    }

    public long JI(Object[] args) {
        return getLong((String) args[0]);
    }

    public long F8(Object[] args) {
        return getLong((String) args[0], (Long) args[1]);
    }

    public List<?> Jx(Object[] args) {
        return getList((String) args[0]);
    }

    public List<?> x_(Object[] args) {
        return getList((String) args[0], (List<?>) args[1]);
    }

    public List<String> JC(Object[] args) {
        return getStringList((String) args[0]);
    }

    public ConfigurationSection JH(Object[] args) {
        return getSection((String) args[0]);
    }
}
