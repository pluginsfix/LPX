package pluginsfix.lpx.gui;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface InventoryProvider {
    long LEGACY_MARKER = 1L;

    Inventory createInventory(GuiInventory gui, org.bukkit.entity.Player player);

    boolean supports(InventoryType type);

    default void populateInventory(Inventory inventory, PaginatedGui page) {
        GuiItem[][] items = page.getItemsGrid();
        if (items == null || items.length == 0) {
            return;
        }

        int columns = items[0].length;
        for (int row = 0; row < items.length; row++) {
            for (int column = 0; column < items[row].length; column++) {
                GuiItem guiItem = items[row][column];
                if (guiItem == null) {
                    continue;
                }
                inventory.setItem(row * columns + column, guiItem.getItemStack());
            }
        }
    }

    default Inventory r(Object[] args) {
        return createInventory((GuiInventory) args[1], (org.bukkit.entity.Player) args[2]);
    }

    default boolean W(Object[] args) {
        return supports((InventoryType) args[0]);
    }

    default void uF(Object[] args) {
        populateInventory((Inventory) args[1], (PaginatedGui) args[2]);
    }
}
