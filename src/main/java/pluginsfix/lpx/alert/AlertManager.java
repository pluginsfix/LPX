package pluginsfix.lpx.alert;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.event.LPXAlertEvent;
import pluginsfix.lpx.api.event.LPXPunishEvent;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.alert.webhook.WebhookSender;
import pluginsfix.lpx.command.legacy.SubCommandNotify;
import pluginsfix.lpx.core.LPXCore;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.util.Timer;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.Set;
import java.util.UUID;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class AlertManager {
    private static final int MAX_QUEUED_FLAGS = 100;
    private final LPXCore core;
    private final FileLogger fileLogger;
    private final WebhookSender webhookSender;
    private final Set<UUID> disabledRecipients;
    private long lastAlertAt;
    private static String legacyState;
    public AlertManager(long ignored, LPXCore core) {
        this.disabledRecipients = new HashSet<>();
        this.lastAlertAt = 0L;
        this.core = core;
        this.fileLogger = new FileLogger(0L, core);
        this.webhookSender = new WebhookSender(core);
    }
    public void flushAll() {
        fileLogger.XY(new Object[]{0L});
        webhookSender.wh(new Object[]{0L});
    }
    public void flushLogger() {
        fileLogger.Xu(new Object[]{0L});
    }
    public void handleAlert(ProtocolPlayer player, Check<?> check, FlagInfo[] infos) {
        if (player.isKicked()) {
            return;
        }
        fileLogger.i0(new Object[]{player, 0L, check, infos});
        pushRecentFlag(player, check, infos);
        runOnMainThread(() -> Bukkit.getPluginManager().callEvent(new LPXAlertEvent(player, check, infos)));
        int minVl = check.getOptions().getCheckOptions().getMinVl();
        if (minVl != -1 && check.getOptions().getVl() < minVl) {
            return;
        }
        AlertConfig alertMessages = core.sZ(new Object[0]).BM(new Object[0]).VM(new Object[0]);
        String alertMessage = formatMessage(alertMessages.nd(new Object[0]), player, check, infos);
        core.getLogger().log(Level.INFO, alertMessage);
        lastAlertAt = System.currentTimeMillis();
        long punishCooldownMs = core.sZ(new Object[0]).Bw(new Object[0]).YK(new Object[0]);
        if (Timer.vS(new Object[]{player.getLastPunish()}) < punishCooldownMs) {
            return;
        }
        player.updateLastAlert();
        TextComponent staffComponent = new TextComponent(alertMessage);
        String hoverText = buildHoverText(alertMessages, player, check, infos);
        if (!hoverText.isEmpty()) {
            staffComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        }
        sendAlertToStaff(staffComponent, alertMessages.nl(new Object[0]));
        FoliaScheduler.getAsyncScheduler().runNow(core.getPlugin(), task ->
            webhookSender.AJ(new Object[]{player, check, 0L, infos})
        );
    }
    public void punishPlayer(ProtocolPlayer player, Check<?> check) {
        long punishCooldownMs = core.sZ(new Object[0]).Bw(new Object[0]).YK(new Object[0]);
        if (Timer.vS(new Object[]{player.getLastPunish()}) < punishCooldownMs) {
            return;
        }
        runOnMainThread(() -> Bukkit.getPluginManager().callEvent(new LPXPunishEvent(player, check)));
        player.updateLastPunish();
        for (String rawCommand : check.getOptions().getCheckOptions().getPunishCommands()) {
            String command = applyPlayerPlaceholders(rawCommand, player);
            if (command.regionMatches(true, 0, "lpx ", 0, 4)) {
                String payload = command.substring(4).trim();
                if (payload.isEmpty()) {
                    continue;
                }
                String[] commandArgs = payload.split("\\s+");
                SubCommandNotify.lP(new Object[]{0L, core, Bukkit.getConsoleSender(), commandArgs});
                continue;
            }
            FoliaScheduler.getGlobalRegionScheduler().execute(
                core.getPlugin(),
                () -> Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), command)
            );
        }
        String punishMessageTemplate = core.sZ(new Object[0]).BM(new Object[0]).V5(new Object[0]);
        String punishMessage = formatMessage(punishMessageTemplate, player, check, new FlagInfo[0]);
        String staffPermission = core.sZ(new Object[0]).BM(new Object[0]).VM(new Object[0]).nl(new Object[0]);
        broadcastToStaff(punishMessage, staffPermission);
    }
    public String formatMessage(String template, ProtocolPlayer player, Check<?> check, FlagInfo[] infos) {
        String withServer = applyServerPlaceholder(template);
        String withPlayer = applyPlayerPlaceholders(withServer, player);
        return applyCheckPlaceholders(withPlayer, check, infos);
    }
    public String applyPlayerPlaceholders(String input, ProtocolPlayer player) {
        return input
            .replace("%player%", player.getUsername())
            .replace("%uuid%", player.getUniqueId().toString());
    }
    public void broadcastToStaff(String message, String permission) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.hasPermission(permission)) {
                continue;
            }
            if (disabledRecipients.contains(online.getUniqueId())) {
                continue;
            }
            online.sendMessage(message);
        }
        core.getLogger().log(Level.INFO, message);
    }
    public boolean toggleAlerts(Player player) {
        UUID uuid = player.getUniqueId();
        if (disabledRecipients.contains(uuid)) {
            disabledRecipients.remove(uuid);
            return true;
        }
        disabledRecipients.add(uuid);
        return false;
    }
    public void removePlayer(UUID uuid) {
        disabledRecipients.remove(uuid);
    }
    public void flushWebhook() {
        webhookSender.wh(new Object[]{0L});
    }
    public FileLogger getFileLogger() {
        return fileLogger;
    }
    public Set<UUID> getDisabledRecipients() {
        return Collections.unmodifiableSet(disabledRecipients);
    }
    public long getLastAlertAt() {
        return lastAlertAt;
    }
    public static void setLegacyState(String value) {
        legacyState = value;
    }
    public static String getLegacyState() {
        return legacyState;
    }
    private void pushRecentFlag(ProtocolPlayer player, Check<?> check, FlagInfo[] infos) {
        if (core.sN(new Object[0]).size() >= MAX_QUEUED_FLAGS) {
            core.sN(new Object[0]).removeLast();
        }
        core.sN(new Object[0]).addFirst(
            new QueuedFlag(
                player.getUsername(),
                check.getOptions(),
                (byte) 0,
                System.currentTimeMillis(),
                0,
                infos,
                0
            )
        );
    }
    private String buildHoverText(AlertConfig alertMessages, ProtocolPlayer player, Check<?> check, FlagInfo[] infos) {
        StringBuilder hoverBuilder = new StringBuilder();
        int totalLines = alertMessages.nt(new Object[0]).size();
        for (int i = 0; i < totalLines; i++) {
            String line = (String) alertMessages.nt(new Object[0]).get(i);
            hoverBuilder.append(formatMessage(line, player, check, infos));
            if (i + 1 < totalLines) {
                hoverBuilder.append('\n');
            }
        }
        return hoverBuilder.toString();
    }
    private String applyCheckPlaceholders(String input, Check<?> check, FlagInfo[] infos) {
        StringBuilder infoBuilder = new StringBuilder();
        if (infos.length == 0) {
            infoBuilder.append("-");
        } else {
            for (int i = 0; i < infos.length; i++) {
                FlagInfo info = infos[i];
                infoBuilder
                    .append(core.sZ(new Object[0]).BM(new Object[0]).Vp(new Object[0]))
                    .append(info.getName())
                    .append(": ")
                    .append(ChatColor.GRAY)
                    .append(String.valueOf(info.getValue()));
                if (i + 1 < infos.length) {
                    infoBuilder.append('\n');
                }
            }
        }
        return input
            .replace("%check%", check.getType().getCategory().getFormattedName())
            .replace("%type%", check.getType().getId().name())
            .replace("%vl%", String.valueOf(check.getOptions().getVl()))
            .replace("%infos%", infoBuilder.toString())
            .replace("%description%", check.getType().getDescription())
            .replace("%max-vl%", String.valueOf(check.getOptions().getCheckOptions().getMaxVL()));
    }
    private String applyServerPlaceholder(String input) {
        return input.replace("%server%", core.getServer());
    }
    private void sendAlertToStaff(TextComponent component, String permission) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.hasPermission(permission)) {
                continue;
            }
            if (disabledRecipients.contains(online.getUniqueId())) {
                continue;
            }
            online.spigot().sendMessage(component);
        }
    }
    private void runOnMainThread(Runnable task) {
        try {
            FoliaScheduler.getGlobalRegionScheduler().execute(core.getPlugin(), task);
        } catch (Throwable ignored) {
            task.run();
        }
    }
    public void WZ(Object[] args) {
        flushAll();
    }
    public void Wr(Object[] args) {
        flushLogger();
    }
    public void Gf(Object... args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        Check<?> check = (Check<?>) args[2];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        handleAlert(player, check, infos);
    }
    public void ip(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        Check<?> check = (Check<?>) args[2];
        punishPlayer(player, check);
    }
    public String n5(Object... args) {
        String template = (String) args[0];
        ProtocolPlayer player = (ProtocolPlayer) args[2];
        Check<?> check = (Check<?>) args[3];
        FlagInfo[] infos = (FlagInfo[]) args[4];
        return formatMessage(template, player, check, infos);
    }
    public String e5(Object[] args) {
        String input = (String) args[1];
        ProtocolPlayer player = (ProtocolPlayer) args[2];
        return applyPlayerPlaceholders(input, player);
    }
    private String u_(Object... args) {
        String input = (String) args[0];
        Check<?> check = (Check<?>) args[2];
        FlagInfo[] infos = (FlagInfo[]) args[3];
        return applyCheckPlaceholders(input, check, infos);
    }
    private void kU(Object[] args) {
        Runnable task = (Runnable) args[0];
        runOnMainThread(task);
    }
    private String JC(Object[] args) {
        String input = (String) args[1];
        return applyServerPlaceholder(input);
    }
    public void v_(Object[] args) {
        String message = (String) args[0];
        String permission = (String) args[1];
        broadcastToStaff(message, permission);
    }
    public boolean ZT(Object[] args) {
        Player player = (Player) args[0];
        return toggleAlerts(player);
    }
    public void HP(Object[] args) {
        UUID uuid = (UUID) args[0];
        removePlayer(uuid);
    }
    public void WA(Object[] args) {
        flushWebhook();
    }
    public FileLogger Wh(Object[] ignored) {
        return getFileLogger();
    }
    public Set WO(Object[] ignored) {
        return disabledRecipients;
    }
    public static void JF(String value) {
        setLegacyState(value);
    }
    public static String W8() {
        return getLegacyState();
    }
}
