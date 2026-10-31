package pluginsfix.lpx.command.impl;

import pluginsfix.lpx.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(LPXCore core) {
        super(core, "reload", "lpx.reload", "Перезагрузить конфигурацию плагина", false, "");
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        core.reloadConfig();
        if (LPX.getInstance() != null) {
            LPX.getInstance().getMessages().send(sender, "reloaded");
        } else {
            sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF00") + "◆ " + org.bukkit.ChatColor.WHITE + "Конфигурация успешно " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "перезагружена" + org.bukkit.ChatColor.WHITE + ".");
        }
    }
}
