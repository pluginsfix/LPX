package pluginsfix.lpx.gui;
import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
public class InventoryViewAdapterLegacy implements InventoryViewAdapter {
    private final InventoryView j;
    private static final long a = 84024649881160L;
    public InventoryViewAdapterLegacy(Player player, long ignored) {
        this.j = player.getOpenInventory();
    }
    @Override
    public boolean isNull() {
        return j == null;
    }
    @Override
    public String getType() {
        return j.getType().name();
    }
    @Override
    public Inventory getTopInventory() {
        return j.getTopInventory();
    }
    @Override
    public Inventory getBottomInventory() {
        return j.getBottomInventory();
    }
    @Override
    public int countSlots() {
        return j.countSlots();
    }
    @Override
    public int convertSlot(int slot) {
        return j.convertSlot(slot);
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
