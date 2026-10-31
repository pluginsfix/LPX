package pluginsfix.lpx.listeners.impl;

import pluginsfix.lpx.gui.GuiInventory;
import pluginsfix.lpx.gui.GuiManagerImpl;
import pluginsfix.lpx.gui.PaginatedGui;
import pluginsfix.lpx.gui.TypedConsumer;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class InventoryEventListener implements Listener {
    private final GuiManagerImpl manager;

    public InventoryEventListener(GuiManagerImpl manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuiInventory gui = manager.getOpenedInventory(player).orElse(null);
        if (gui == null) {
            return;
        }

        if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR
            || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
            return;
        }

        if (manager.getCore().getInventoryViewAdapter(player).getTopInventory() != event.getClickedInventory()) {
            return;
        }

        event.setCancelled(true);
        dispatch(gui, InventoryClickEvent.class, event);

        int slot = event.getSlot();
        int columns = gui.getColumns();
        int row = slot / columns;
        int column = slot % columns;

        if (row < 0 || column < 0 || row >= gui.getRows() || column >= columns) {
            player.updateInventory();
            return;
        }

        PaginatedGui page = manager.getOpenedPage(player).orElse(null);
        if (page != null) {
            page.getItemAt(row, column).ifPresent(item -> item.handleClick(event));
        }

        player.updateInventory();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        GuiInventory gui = manager.getOpenedInventory(player).orElse(null);
        if (gui == null) {
            return;
        }

        int topSize = manager.getCore().getInventoryViewAdapter(player).getTopInventory().getSize();
        for (int rawSlot : event.getRawSlots()) {
            if (rawSlot < topSize) {
                event.setCancelled(true);
                break;
            }
        }

        dispatch(gui, InventoryDragEvent.class, event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        GuiInventory gui = manager.getOpenedInventory(player).orElse(null);
        if (gui == null) {
            return;
        }

        dispatch(gui, InventoryOpenEvent.class, event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        GuiInventory gui = manager.getOpenedInventory(player).orElse(null);
        if (gui == null) {
            return;
        }

        dispatch(gui, InventoryCloseEvent.class, event);

        if (gui.isReopenOnClose()) {
            FoliaScheduler.getEntityScheduler().run(
                (Entity) player,
                manager.getPlugin(),
                task -> player.openInventory(event.getInventory()),
                () -> {
                }
            );
            return;
        }

        manager.clearOpenedState(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GuiInventory gui = manager.getOpenedInventory(player).orElse(null);
        if (gui == null) {
            return;
        }

        dispatch(gui, PlayerQuitEvent.class, event);
        manager.clearOpenedState(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPluginDisable(PluginDisableEvent event) {
        Map<Player, GuiInventory> opened = manager.getOpenedInventoriesSnapshot();
        for (Map.Entry<Player, GuiInventory> entry : opened.entrySet()) {
            Player player = entry.getKey();
            GuiInventory gui = entry.getValue();
            dispatch(gui, PluginDisableEvent.class, event);
            gui.close(player);
        }
    }

    private <E extends Event> void dispatch(GuiInventory gui, Class<E> eventType, E event) {
        List<TypedConsumer<? extends Event>> consumers = gui.getEventConsumers();
        for (TypedConsumer<? extends Event> consumer : consumers) {
            if (consumer.Fn(new Object[0]) == eventType) {
                consumer.pr(new Object[]{event});
            }
        }
    }
}
