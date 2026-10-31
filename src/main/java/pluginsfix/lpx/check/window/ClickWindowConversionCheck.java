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
public class ClickWindowConversionCheck extends AbstractCheckT<WrapperPlayClientClickWindow> {
    public ClickWindowConversionCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkConversion(args);
    }
    public CheckResult checkConversion(Object[] args) {
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
        int button = packet.getButton();
        int slotCount = view.countSlots();
        WrapperPlayClientClickWindow.WindowClickType clickType = packet.getWindowClickType();
        boolean invalid = false;
        if (slot >= slotCount) {
            invalid = true;
        }
        if (clickType == WrapperPlayClientClickWindow.WindowClickType.SWAP && (slot < 0 || button < 0 || button > 8)) {
            invalid = true;
        }
        if (slot < -999) {
            invalid = true;
        }
        if (invalid) {
            int convertedSlot = convertSlotSafe(view, slot);
            Inventory top = view.getTopInventory();
            Inventory bottom = view.getBottomInventory();
            AH(new Object[]{
                0,
                token,
                packet,
                new FlagInfo[]{
                    FlagInfo.of("action_number", packet.getActionNumber().orElse(null)),
                    FlagInfo.of("click_type", clickType == null ? "null" : clickType.name()),
                    FlagInfo.of("button", button),
                    FlagInfo.of("slot", slot),
                    FlagInfo.of("converted_slot", convertedSlot),
                    FlagInfo.of("slot_count", slotCount),
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
    private int convertSlotSafe(InventoryViewAdapter view, int slot) {
        if (slot < 0) {
            return -1;
        }
        try {
            return view.convertSlot(slot);
        } catch (RuntimeException ignored) {
            return -1;
        }
    }
}
