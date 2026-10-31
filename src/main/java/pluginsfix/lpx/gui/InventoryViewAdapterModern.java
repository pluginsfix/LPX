package pluginsfix.lpx.gui;
import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
public class InventoryViewAdapterModern implements InventoryViewAdapter {
    private final InventoryView e;
    private static PacketWrapper[] C;
    private static final long a = 108655777277795L;
    public InventoryViewAdapterModern(Player player, long ignored) {
        this.e = player.getOpenInventory();
    }
    @Override
    public boolean isNull() {
        return e == null;
    }
    @Override
    public String getType() {
        return e.getType().name();
    }
    @Override
    public Inventory getTopInventory() {
        return e.getTopInventory();
    }
    @Override
    public Inventory getBottomInventory() {
        return e.getBottomInventory();
    }
    @Override
    public int countSlots() {
        return e.countSlots();
    }
    @Override
    public int convertSlot(int slot) {
        return e.convertSlot(slot);
    }
    public static void cL(PacketWrapper[] packetWrappers) {
        C = packetWrappers;
    }
    public static PacketWrapper[] Ph() {
        return C;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
