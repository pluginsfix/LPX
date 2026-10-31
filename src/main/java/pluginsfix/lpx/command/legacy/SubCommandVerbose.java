package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.command.impl.VerboseCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public class SubCommandVerbose extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    public SubCommandVerbose(LPXCore core, long ignored) {
        super(core, "verbose", "lpx.verbose", "Show current network stats", false, "");
        this.delegate = new VerboseCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
}
