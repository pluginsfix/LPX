package pluginsfix.lpx.command.impl;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class KickCommand extends SubCommand {
    private static PacketWrapper[] legacyCache;
    public KickCommand(LPXCore core) {
        super(core, "kick", "lpx.kick", "Kick a player", false, "<player> <reason...>");
    }
    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        kickFromCommand(core, sender, args);
    }
    public static void kickFromCommand(LPXCore core, CommandSender sender, String[] commandArgs) {
        lP(new Object[]{0L, core, sender, commandArgs});
    }
    public static void lP(Object[] args) {
        LPXCore core = (LPXCore) args[1];
        CommandSender sender = (CommandSender) args[2];
        String[] commandArgs = (String[]) args[3];
        if (commandArgs.length < 2) {
            sender.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).V8(new Object[0])
                + ChatColor.RED + " Usage: /lpx kick <player> <reason...>");
            return;
        }
        Player target = Bukkit.getPlayerExact(commandArgs[1]);
        if (target == null) {
            sender.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).V8(new Object[0])
                + ChatColor.RED + " Player '" + commandArgs[1] + "' is offline.");
            return;
        }
        String reason;
        if (commandArgs.length <= 2) {
            reason = core.sZ(new Object[0]).BM(new Object[0]).Vv(new Object[0]);
        } else {
            reason = String.join(" ", Arrays.copyOfRange(commandArgs, 2, commandArgs.length));
        }
        if (reason == null || reason.trim().isEmpty()) {
            reason = core.sZ(new Object[0]).BM(new Object[0]).Vv(new Object[0]);
        }
        core.getLogger().info(() -> "Kicking player " + target.getName() + " with LPX command.");
        core.Cs(new Object[]{target, reason});
    }
    public static void dP(PacketWrapper[] wrappers) {
        legacyCache = wrappers;
    }
    public static PacketWrapper[] BL() {
        return legacyCache;
    }
}
