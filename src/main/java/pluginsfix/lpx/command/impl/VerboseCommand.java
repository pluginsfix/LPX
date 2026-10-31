package pluginsfix.lpx.command.impl;

import pluginsfix.lpx.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.network.NetworkStatsCollector;
import pluginsfix.lpx.stats.StatsFormatter;
import org.bukkit.command.CommandSender;

public class VerboseCommand extends SubCommand {
    private final NetworkStatsCollector collector;
    private final StatsFormatter formatter;

    public VerboseCommand(LPXCore core) {
        super(core, "verbose", "lpx.verbose", "Отобразить текущую сетевую статистику", false, "");
        this.collector = core.sq(new Object[0]);
        this.formatter = new StatsFormatter(0L, core);
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        if (!collector.nG(new Object[0])) {
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "verbose-disabled");
            } else {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FB8808") + "▶ " + org.bukkit.ChatColor.WHITE + "Сборщик статистики отключён. Используйте " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "/lpx info" + org.bukkit.ChatColor.WHITE + ".");
            }
            return;
        }
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task -> {
            String payload = formatter.wm(new Object[]{0L, collector});
            sender.sendMessage(payload);
        });
    }
}
