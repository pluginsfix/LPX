package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.network.NetworkStatsCollector;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
public class NetworkInfoCommand extends SubCommand {
    public NetworkInfoCommand(LPXCore core) {
        super(core, "info", "lpx.info", "Toggle network collector", false, "");
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task -> {
            NetworkStatsCollector collector = core.sq(new Object[0]);
            if (collector.nG(new Object[0])) {
                collector.nz(new Object[]{0L});
                sendPrefixedMessage(sender, ChatColor.RED + "Network collector disabled.");
                return;
            }
            collector.nW(new Object[]{0L});
            sendPrefixedMessage(sender, ChatColor.GREEN + "Network collector enabled.");
        });
    }
}
