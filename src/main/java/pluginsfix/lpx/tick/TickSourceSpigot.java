package pluginsfix.lpx.tick;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TickSourceSpigot extends AbstractTickSource implements TickSource, Runnable {
    private final Plugin plugin;
    private BukkitTask task;

    public TickSourceSpigot(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        tick();
    }

    @Override
    public void start() {
        this.task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 1L, 1L);
    }

    @Override
    public void close() {
        if (task != null) {
            task.cancel();
        }
    }
}
