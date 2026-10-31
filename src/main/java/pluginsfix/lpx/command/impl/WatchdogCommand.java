package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.network.WatchdogMonitor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
public class WatchdogCommand extends SubCommand {
    public WatchdogCommand(LPXCore core) {
        super(core, "watchdog", "lpx.debug", "Toggle Netty watchdog monitor", false, "");
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task -> {
            WatchdogMonitor monitor = core.s9(new Object[0]);
            boolean alreadyRunning = monitor.tW(new Object[0]);
            monitor.tN(new Object[]{0L});
            if (alreadyRunning) {
                sendPrefixedMessage(sender, ChatColor.RED + "Netty watchdog disabled.");
                return;
            }
            sendPrefixedMessage(sender, ChatColor.GREEN + "Netty watchdog enabled.");
        });
    }
}
