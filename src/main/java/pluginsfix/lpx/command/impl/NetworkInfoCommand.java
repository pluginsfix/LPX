package pluginsfix.lpx.command.impl;

import pluginsfix.lpx.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.network.NetworkStatsCollector;
import org.bukkit.command.CommandSender;

public class NetworkInfoCommand extends SubCommand {
    public NetworkInfoCommand(LPXCore core) {
        super(core, "info", "lpx.info", "Включить/выключить сбор сетевой статистики", false, "");
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task -> {
            NetworkStatsCollector collector = core.sq(new Object[0]);
            if (collector.nG(new Object[0])) {
                collector.nz(new Object[]{0L});
                if (LPX.getInstance() != null) {
                    LPX.getInstance().getMessages().send(sender, "network-info-disabled");
                } else {
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FB8808") + "▶ " + org.bukkit.ChatColor.WHITE + "Сборщик сетевой статистики " + net.md_5.bungee.api.ChatColor.of("#FB8808") + "отключён" + org.bukkit.ChatColor.WHITE + ".");
                }
                return;
            }
            collector.nW(new Object[]{0L});
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "network-info-enabled");
            } else {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF00") + "◆ " + org.bukkit.ChatColor.WHITE + "Сборщик сетевой статистики " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "включён" + org.bukkit.ChatColor.WHITE + ".");
            }
        });
    }
}
