package pluginsfix.lpx.listeners.impl;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.player.PrinterData;
import pluginsfix.lpx.api.player.PrinterStatus;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.config.PrinterConfig;
import pluginsfix.lpx.core.LPXCore;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
public class BukkitEventListener implements Listener {
    private final LPXCore core;
    public BukkitEventListener(LPXCore core) {
        this.core = core;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (core.getFirstJoinTime() == 0L) {
            core.setFirstJoinTime(System.currentTimeMillis());
        }
        if (core.sZ(new Object[0]).Bw(new Object[0]).Yh(new Object[0])
            && !core.isUpdated()
            && player.hasPermission("lpx.update")) {
            player.sendMessage(core.sZ(new Object[0]).BM(new Object[0]).Vq(new Object[0]));
        }
        if (!core.sZ(new Object[0]).Bw(new Object[0]).YG(new Object[0])) {
            core.s1(new Object[0]).toggleAlerts(player);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        PrinterConfig printerConfig = core.sZ(new Object[0]).Bw(new Object[0]).Y1(new Object[0]);
        if (!printerConfig.xk(new Object[0])) {
            return;
        }
        ProtocolPlayer protocolPlayer = core.getRegistry().getPlayer(event.getPlayer()).orElse(null);
        if (protocolPlayer == null) {
            return;
        }
        PrinterData printerData = protocolPlayer.getPrinterData();
        printerData.updateLastPlace();
        if (printerData.isUsingPrinter()) {
            return;
        }
        if (printerData.increaseBlocksPlaced() >= printerConfig.x2(new Object[0])) {
            printerData.setPrinterMode(PrinterStatus.AUTOMATIC);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void commandExecute(PlayerCommandPreprocessEvent event) {
        ProtocolPlayer protocolPlayer = core.getRegistry().getPlayer(event.getPlayer()).orElse(null);
        if (protocolPlayer == null || protocolPlayer.ignoreChecks()) {
            return;
        }
        if (protocolPlayer.handleChecks(event.getClass(), event).result() == CheckResult.DENY) {
            event.setCancelled(true);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void portalTeleport(PlayerTeleportEvent event) {
        if (!core.sZ(new Object[0]).Bw(new Object[0]).Y5(new Object[0])) {
            return;
        }
        PlayerTeleportEvent.TeleportCause cause = event.getCause();
        if (cause != PlayerTeleportEvent.TeleportCause.END_PORTAL
            && cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }
        ProtocolPlayer protocolPlayer = core.getRegistry().getPlayer(event.getPlayer()).orElse(null);
        if (protocolPlayer == null || protocolPlayer.ignoreChecks()) {
            return;
        }
        int delayMs = core.sZ(new Object[0]).Bw(new Object[0]).YS(new Object[0]);
        if (System.currentTimeMillis() - protocolPlayer.getLastPortalTeleport() <= delayMs) {
            event.setCancelled(true);
            return;
        }
        protocolPlayer.updateLastPortalTeleport();
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void teleportEvent(PlayerTeleportEvent event) {
        core.getRegistry().getPlayer(event.getPlayer()).ifPresent(ProtocolPlayer::updateLastTeleport);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void respawnEvent(PlayerRespawnEvent event) {
        core.getRegistry().getPlayer(event.getPlayer()).ifPresent(ProtocolPlayer::updateLastTeleport);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void projectileLaunch(ProjectileLaunchEvent event) {
        if (!core.sZ(new Object[0]).Bw(new Object[0]).Ya(new Object[0])) {
            return;
        }
        Projectile projectile = event.getEntity();
        if (projectile instanceof Arrow
            && projectile.getVelocity().lengthSquared() > core.sZ(new Object[0]).Bw(new Object[0]).Y6(new Object[0])) {
            event.setCancelled(true);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void shearEntity(PlayerShearEntityEvent event) {
        if (!core.sZ(new Object[0]).Bw(new Object[0]).YA(new Object[0])) {
            return;
        }
        ProtocolPlayer protocolPlayer = core.getRegistry().getPlayer(event.getPlayer()).orElse(null);
        if (protocolPlayer == null || protocolPlayer.ignoreChecks()) {
            return;
        }
        int cooldownMs = core.sZ(new Object[0]).Bw(new Object[0]).Yz(new Object[0]);
        if (System.currentTimeMillis() - protocolPlayer.getLastShears() <= cooldownMs) {
            event.setCancelled(true);
            return;
        }
        protocolPlayer.updateLastShears();
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void respawnEvent(BlockBreakEvent event) {
        if (!core.sZ(new Object[0]).Bw(new Object[0]).YB(new Object[0])) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getOpenInventory() != null) {
            player.closeInventory();
        }
    }
}
