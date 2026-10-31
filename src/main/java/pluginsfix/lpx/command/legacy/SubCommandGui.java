package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.command.impl.GuiCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public class SubCommandGui extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    public SubCommandGui(LPXCore core, long ignored) {
        super(core, "gui", "lpx.gui", "Open LPX GUI", true, "");
        this.delegate = new GuiCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
}
