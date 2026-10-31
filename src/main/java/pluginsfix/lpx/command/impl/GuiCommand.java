package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class GuiCommand extends SubCommand {
    public GuiCommand(LPXCore core) {
        super(core, "gui", "lpx.gui", "Open LPX GUI", true, "");
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        core.sx(new Object[0]).Ou(new Object[0]).lq(new Object[]{0L, (Player) sender});
    }
}
