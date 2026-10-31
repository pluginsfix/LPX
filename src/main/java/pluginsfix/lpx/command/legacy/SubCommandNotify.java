package pluginsfix.lpx.command.legacy;
import pluginsfix.lpx.command.impl.KickCommand;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.command.CommandSender;
public class SubCommandNotify extends SubCommand {
    private final pluginsfix.lpx.command.SubCommand delegate;
    private static PacketWrapper[] legacyCache = new PacketWrapper[0];
    public SubCommandNotify(long ignored, LPXCore core) {
        super(core, "kick", "lpx.kick", "Kick a player", false, "<player> <reason...>");
        this.delegate = new KickCommand(core);
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        delegate.execute(sender, args);
    }
    public static void lP(Object[] args) {
        KickCommand.lP(args);
    }
    public static void dP(PacketWrapper[] wrappers) {
        legacyCache = wrappers;
        KickCommand.dP(wrappers);
    }
    public static PacketWrapper[] BL() {
        return legacyCache;
    }
}
