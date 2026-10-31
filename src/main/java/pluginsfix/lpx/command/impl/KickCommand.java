package pluginsfix.lpx.command.impl;

import pluginsfix.lpx.LPX;
import pluginsfix.lpx.command.SubCommand;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.text.Placeholder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends SubCommand {
    private static PacketWrapper[] legacyCache;

    public KickCommand(LPXCore core) {
        super(core, "kick", "lpx.kick", "Кикнуть подозрительного игрока", false, "<игрок> [причина]");
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
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "kick-usage");
            } else {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FB8808") + "▶ " + org.bukkit.ChatColor.WHITE + "Использование: " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "/lpx kick <игрок> [причина]");
            }
            return;
        }
        Player target = Bukkit.getPlayerExact(commandArgs[1]);
        if (target == null) {
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "player-offline", Placeholder.of("player", commandArgs[1]));
            } else {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FB8808") + "▶ " + org.bukkit.ChatColor.WHITE + "Игрок " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + commandArgs[1] + org.bukkit.ChatColor.WHITE + " сейчас оффлайн.");
            }
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
        if (LPX.getInstance() != null) {
            LPX.getInstance().getMessages().send(sender, "kick-success", Placeholder.of("player", target.getName()));
        } else {
            sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF00") + "◆ " + org.bukkit.ChatColor.WHITE + "Игрок " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + target.getName() + org.bukkit.ChatColor.WHITE + " был успешно кикнут.");
        }
    }

    public static void dP(PacketWrapper[] wrappers) {
        legacyCache = wrappers;
    }

    public static PacketWrapper[] BL() {
        return legacyCache;
    }
}
