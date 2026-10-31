package pluginsfix.lpx.api;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.Server;
public interface LPXPlugin
extends Plugin {
    public Logger getLogger();
    public File getDataFolder();
    public File getConfigFile();
    public InputStream getResource(String var1);
    public PluginDescriptionFile getDescription();
    public Server getServer();
    public FileConfiguration getConfig();
    public void reload();
    public void saveDefaultConfig();
    public void saveConfig();
}
