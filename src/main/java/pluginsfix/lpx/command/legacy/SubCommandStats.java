package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.command.impl.AlertsCommand;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.command.CommandSender;
public class SubCommandStats extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    public SubCommandStats(long ignored, LPXCore core) {
        super(
            core,
            "alerts",
            core.sZ(new Object[0]).BM(new Object[0]).VM(new Object[0]).nl(new Object[0]),
            "Toggle alerts",
            true,
            ""
        );
        this.delegate = new AlertsCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
}
