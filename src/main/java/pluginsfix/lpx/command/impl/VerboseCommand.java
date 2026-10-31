package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.network.NetworkStatsCollector;
import pluginsfix.lpx.stats.StatsFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
public class VerboseCommand extends SubCommand {
    private final NetworkStatsCollector collector;
    private final StatsFormatter formatter;
    public VerboseCommand(LPXCore core) {
        super(core, "verbose", "lpx.verbose", "Show current network stats", false, "");
        this.collector = core.sq(new Object[0]);
        this.formatter = new StatsFormatter(0L, core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        if (!collector.nG(new Object[0])) {
            sendPrefixedMessage(sender, ChatColor.RED + "Network collector is disabled. Use /lpx info first.");
            return;
        }
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task -> {
            String payload = formatter.wm(new Object[]{0L, collector});
            sender.sendMessage(payload);
        });
    }
}
