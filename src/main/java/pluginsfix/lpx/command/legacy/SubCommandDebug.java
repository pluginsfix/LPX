package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.command.impl.WatchdogCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public class SubCommandDebug extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    public SubCommandDebug(LPXCore core, long ignored) {
        super(core, "watchdog", "lpx.debug", "Toggle Netty watchdog monitor", false, "");
        this.delegate = new WatchdogCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
}
