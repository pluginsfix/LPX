package pluginsfix.lpx.api.adapter;
import org.bukkit.inventory.Inventory;
public interface InventoryViewAdapter {
    public boolean isNull();
    public String getType();
    public Inventory getTopInventory();
    public Inventory getBottomInventory();
    public int countSlots();
    public int convertSlot(int var1);
}
