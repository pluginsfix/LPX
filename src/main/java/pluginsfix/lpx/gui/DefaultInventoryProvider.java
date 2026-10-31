package pluginsfix.lpx.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class DefaultInventoryProvider implements InventoryProvider {
    @Override
    public Inventory createInventory(GuiInventory gui, Player player) {
        Inventory inventory = Bukkit.createInventory(
            (InventoryHolder) player,
            gui.getRows() * gui.getColumns(),
            gui.getTitle()
        );

        GuiManagerImpl manager = gui.getManager();
        PaginatedGui page = manager.getOpenedPage(player).orElse(null);
        if (page != null) {
            populateInventory(inventory, page);
        }

        player.openInventory(inventory);
        return inventory;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST;
    }
}
