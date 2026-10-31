package pluginsfix.lpx.command.impl;

import pluginsfix.lpx.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.network.WatchdogMonitor;
import org.bukkit.command.CommandSender;

public class WatchdogCommand extends SubCommand {
    public WatchdogCommand(LPXCore core) {
        super(core, "watchdog", "lpx.debug", "Переключить Netty Watchdog мониторинг", false, "");
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task -> {
            WatchdogMonitor monitor = core.s9(new Object[0]);
            boolean alreadyRunning = monitor.tW(new Object[0]);
            monitor.tN(new Object[]{0L});
            if (alreadyRunning) {
                if (LPX.getInstance() != null) {
                    LPX.getInstance().getMessages().send(sender, "watchdog-disabled");
                } else {
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FB8808") + "▶ " + org.bukkit.ChatColor.WHITE + "Мониторинг Netty Watchdog " + net.md_5.bungee.api.ChatColor.of("#FB8808") + "отключён" + org.bukkit.ChatColor.WHITE + ".");
                }
                return;
            }
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "watchdog-enabled");
            } else {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF00") + "◆ " + org.bukkit.ChatColor.WHITE + "Мониторинг Netty Watchdog " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "включён" + org.bukkit.ChatColor.WHITE + ".");
            }
        });
    }
}
