package pluginsfix.lpx.tick;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class TickSourcePaper extends AbstractTickSource implements TickSource, Listener {
    private final Plugin plugin;

    public TickSourcePaper(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerTickEvent(ServerTickStartEvent event) {
        tick();
    }

    @Override
    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void close() {
        HandlerList.unregisterAll(this);
    }
}
