package pluginsfix.lpx.listeners.impl;

import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.config.PacketEventsConfig;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.util.BlockStateAccessor;
import pluginsfix.lpx.util.Timer;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class ContainerInteractListener implements Listener {
    private final LPXCore core;

    public ContainerInteractListener(LPXCore core) {
        this.core = core;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onContainerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        if (!(BlockStateAccessor.yO(new Object[]{0L, event.getClickedBlock()}) instanceof InventoryHolder)) {
            return;
        }

        PacketEventsConfig options = core.sZ(new Object[0]).Bw(new Object[0]);
        if (!options.isInteractContainerDelayEnabled()) {
            return;
        }

        ProtocolPlayer protocolPlayer = core.getRegistry().getPlayer(event.getPlayer()).orElse(null);
        if (protocolPlayer == null) {
            return;
        }
        if (protocolPlayer.getPrinterData().isUsingPrinter()) {
            return;
        }

        long elapsed = Timer.vS(new Object[]{protocolPlayer.getLastContainerInteract()});
        if (elapsed < options.getInteractContainerDelayMs()) {
            event.setUseInteractedBlock(Event.Result.DENY);
            return;
        }

        if (!event.isCancelled()) {
            protocolPlayer.updateLastContainerInteract();
        }
    }
}
