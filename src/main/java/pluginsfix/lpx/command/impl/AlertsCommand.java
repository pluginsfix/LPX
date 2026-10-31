package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class AlertsCommand extends SubCommand {
    public AlertsCommand(LPXCore core) {
        super(
            core,
            "alerts",
            core.sZ(new Object[0]).BM(new Object[0]).VM(new Object[0]).nl(new Object[0]),
            "Toggle alerts",
            true,
            ""
        );
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        boolean nowEnabled = core.s1(new Object[0]).toggleAlerts((Player) sender);
        if (nowEnabled) {
            sender.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).VM(new Object[0]).np(new Object[0]));
            return;
        }
        sender.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).VM(new Object[0]).nh(new Object[0]));
    }
}
