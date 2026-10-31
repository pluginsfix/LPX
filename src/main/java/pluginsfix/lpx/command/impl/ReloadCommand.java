package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
public class ReloadCommand extends SubCommand {
    public ReloadCommand(LPXCore core) {
        super(core, "reload", "lpx.reload", "Reload plugin configuration", false, "");
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        core.reloadConfig();
        sendPrefixedMessage(sender, ChatColor.GREEN + "Configuration reloaded.");
    }
}
