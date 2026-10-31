package pluginsfix.lpx.command;

import pluginsfix.lpx.LPX;
import pluginsfix.lpx.command.impl.AlertsCommand;
import pluginsfix.lpx.command.impl.GuiCommand;
import pluginsfix.lpx.command.impl.KickCommand;
import pluginsfix.lpx.command.impl.NetworkInfoCommand;
import pluginsfix.lpx.command.impl.ReloadCommand;
import pluginsfix.lpx.command.impl.VerboseCommand;
import pluginsfix.lpx.command.impl.WatchdogCommand;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.text.Placeholder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRouter implements CommandExecutor, TabCompleter {
    private final LPXCore core;
    private final Map<String, SubCommand> commands;

    public CommandRouter(short ignoredA, short ignoredB, int ignoredC, LPXCore core) {
        this(core);
    }

    public CommandRouter(LPXCore core) {
        this.core = core;
        this.commands = new LinkedHashMap<>();
        JavaPlugin plugin = (JavaPlugin) core.getPlugin();
        PluginCommand cmd = plugin.getCommand("lpx");
        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
        register(new ReloadCommand(core));
        register(new GuiCommand(core));
        register(new AlertsCommand(core));
        register(new KickCommand(core));
        register(new NetworkInfoCommand(core));
        register(new VerboseCommand(core));
        register(new WatchdogCommand(core));
        alias("stats", "verbose");
        alias("notify", "kick");
        alias("debug", "watchdog");
    }

    private void register(SubCommand command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    private void alias(String alias, String target) {
        SubCommand targetCommand = commands.get(target.toLowerCase());
        if (targetCommand != null) {
            commands.put(alias.toLowerCase(), targetCommand);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean hasVisibleCommand = hasVisibleCommand(sender);
        if (!hasVisibleCommand && core.sZ(new Object[0]).Bw(new Object[0]).Yr(new Object[0])) {
            sender.sendMessage(getUnknownCommandMessage());
            return true;
        }
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        SubCommand subCommand = commands.get(args[0].toLowerCase());
        if (subCommand == null) {
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "invalid-arguments");
            } else {
                sender.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).Vl(new Object[0]));
            }
            return true;
        }
        if (!sender.hasPermission(subCommand.getPermission())) {
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "no-permission", Placeholder.of("permission", subCommand.getPermission()));
            } else {
                sender.sendMessage(
                    core.sZ(new Object[0]).BM(new Object[0]).VH(new Object[0])
                        .replace("%permission%", subCommand.getPermission())
                );
            }
            return true;
        }
        if (subCommand.isPlayerOnly() && !(sender instanceof Player)) {
            if (LPX.getInstance() != null) {
                LPX.getInstance().getMessages().send(sender, "player-only");
            } else {
                sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FB8808") + "▶ " + ChatColor.WHITE + "Команда доступна только игрокам.");
            }
            return true;
        }
        subCommand.execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            List<String> list = new ArrayList<>();
            for (SubCommand subCommand : uniqueCommands()) {
                if (sender.hasPermission(subCommand.getPermission()) && subCommand.getName().toLowerCase().startsWith(prefix)) {
                    list.add(subCommand.getName());
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    private boolean hasVisibleCommand(CommandSender sender) {
        for (SubCommand subCommand : uniqueCommands()) {
            if (sender.hasPermission(subCommand.getPermission())) {
                if (!subCommand.isPlayerOnly() || sender instanceof Player) {
                    return true;
                }
            }
        }
        return false;
    }

    private void sendHelp(CommandSender sender) {
        if (LPX.getInstance() != null) {
            LPX.getInstance().getMessages().send(sender, "help");
            return;
        }
        sender.sendMessage(net.md_5.bungee.api.ChatColor.of("#FFFF00") + "▶ " + ChatColor.WHITE + "Доступные команды " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "LPX" + ChatColor.WHITE + ":");
        for (SubCommand subCommand : uniqueCommands()) {
            if (!sender.hasPermission(subCommand.getPermission())) {
                continue;
            }
            if (subCommand.isPlayerOnly() && !(sender instanceof Player)) {
                continue;
            }
            String usage = subCommand.getUsage();
            String usageSuffix = usage == null || usage.isEmpty() ? "" : " " + usage;
            sender.sendMessage(
                "  " + net.md_5.bungee.api.ChatColor.of("#FFFF00") + "◆ " + ChatColor.WHITE + "/lpx " + subCommand.getName() + usageSuffix
                    + ChatColor.DARK_GRAY + " — " + ChatColor.WHITE + subCommand.getDescription()
            );
        }
    }

    private Collection<SubCommand> uniqueCommands() {
        Map<String, SubCommand> unique = new LinkedHashMap<>();
        unique.put("reload", commands.get("reload"));
        unique.put("gui", commands.get("gui"));
        unique.put("alerts", commands.get("alerts"));
        unique.put("kick", commands.get("kick"));
        unique.put("info", commands.get("info"));
        unique.put("verbose", commands.get("verbose"));
        unique.put("watchdog", commands.get("watchdog"));
        unique.values().removeIf(v -> v == null);
        return unique.values();
    }

    private String getUnknownCommandMessage() {
        try {
            String fromSpigot = core.getPlugin().getServer().spigot().getSpigotConfig().getString("messages.unknown-command");
            if (fromSpigot != null && !fromSpigot.isEmpty()) {
                return ChatColor.translateAlternateColorCodes('&', fromSpigot);
            }
        } catch (Throwable ignored) {
        }
        if (LPX.getInstance() != null) {
            return LPX.getInstance().getMessages().get("unknown-command");
        }
        return core.sZ(new Object[0]).BM(new Object[0]).VJ(new Object[0]);
    }
}
