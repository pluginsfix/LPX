package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.command.impl.ReloadCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public class SubCommandReload extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    public SubCommandReload(LPXCore core, long ignored) {
        super(core, "reload", "lpx.reload", "Reload plugin configuration", false, "");
        this.delegate = new ReloadCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
}
