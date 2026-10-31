package pluginsfix.lpx.gui;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiTypeRegistry implements InventoryProvider {
    private static final List<InventoryType> SUPPORTED_TYPES = ImmutableList.of(
        InventoryType.FURNACE,
        InventoryType.WORKBENCH,
        InventoryType.DISPENSER,
        InventoryType.DROPPER,
        InventoryType.ENCHANTING,
        InventoryType.BREWING,
        InventoryType.ANVIL,
        InventoryType.BEACON,
        InventoryType.HOPPER
    );

    private static String legacyState;

    @Override
    public Inventory createInventory(GuiInventory gui, Player player) {
        Inventory inventory = Bukkit.createInventory(
            (InventoryHolder) player,
            gui.getInventoryType(),
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
        return SUPPORTED_TYPES.contains(type);
    }

    public static void setLegacyState(String value) {
        legacyState = value;
    }

    public static String getLegacyState() {
        return legacyState;
    }

    public static void _D(String value) {
        setLegacyState(value);
    }

    public static String YE() {
        return getLegacyState();
    }
}
