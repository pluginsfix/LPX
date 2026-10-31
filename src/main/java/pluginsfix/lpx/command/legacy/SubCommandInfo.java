package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.command.impl.NetworkInfoCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public class SubCommandInfo extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    public SubCommandInfo(long ignoredA, byte ignoredB, LPXCore core) {
        super(core, "info", "lpx.info", "Toggle network collector", false, "");
        this.delegate = new NetworkInfoCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
}
