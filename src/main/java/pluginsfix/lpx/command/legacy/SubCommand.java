package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.core.LPXCore;
@Deprecated
public abstract class SubCommand extends pluginsfix.lpx.command.SubCommand {
    protected SubCommand(LPXCore core, String name, String permission, String description, boolean playerOnly) {
        super(core, name, permission, description, playerOnly);
    }
    protected SubCommand(LPXCore core, String name, String permission, String description, boolean playerOnly, String usage) {
        super(core, name, permission, description, playerOnly, usage);
    }
}
