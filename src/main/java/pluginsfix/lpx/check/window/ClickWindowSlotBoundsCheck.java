package pluginsfix.lpx.check.window;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
public class ClickWindowSlotBoundsCheck extends AbstractCheckT<WrapperPlayClientClickWindow> {
    public ClickWindowSlotBoundsCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkSlotBounds(args);
    }
    public CheckResult checkSlotBounds(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientClickWindow packet = (WrapperPlayClientClickWindow) args[2];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        InventoryViewAdapter view = protocolPlayer.lpx().getInventoryViewAdapter(bukkitPlayer);
        if (view == null || view.isNull()) {
            return CheckResult.ALLOW;
        }
        int slot = packet.getSlot();
        int maxSlots = view.countSlots();
        if (isOutsideSlot(slot)) {
            jv(new Object[0]);
            return CheckResult.ALLOW;
        }
        if (slot < 0 || slot >= maxSlots) {
            Inventory top = view.getTopInventory();
            Inventory bottom = view.getBottomInventory();
            AH(new Object[]{
                0,
                token,
                packet,
                new FlagInfo[]{
                    FlagInfo.of("slot", slot),
                    FlagInfo.of("max_slots", maxSlots),
                    FlagInfo.of("type", view.getType()),
                    FlagInfo.of("top_inventory", top == null ? "null" : top.getType().name()),
                    FlagInfo.of("bottom_inventory", bottom == null ? "null" : bottom.getType().name())
                }
            });
            return CheckResult.DENY;
        }
        jv(new Object[0]);
        return CheckResult.ALLOW;
    }
    private boolean isOutsideSlot(int slot) {
        return slot == -999 || slot == -1;
    }
}
