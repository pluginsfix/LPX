package pluginsfix.lpx.check.window;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.entity.Player;
public class ClickWindowTypeCheck extends AbstractCheckT<WrapperPlayClientClickWindow> {
    public ClickWindowTypeCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkWindowType(args);
    }
    public CheckResult checkWindowType(Object[] args) {
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
        WrapperPlayClientClickWindow.WindowClickType clickType = packet.getWindowClickType();
        if (clickType == null) {
            AH(new Object[]{
                0,
                token,
                packet,
                new FlagInfo[]{
                    FlagInfo.of("type", view.getType()),
                    FlagInfo.of("click_type", "null")
                }
            });
            return CheckResult.DENY;
        }
        if (clickType == WrapperPlayClientClickWindow.WindowClickType.QUICK_MOVE && view.countSlots() <= 0) {
            AH(new Object[]{
                0,
                token,
                packet,
                new FlagInfo[]{
                    FlagInfo.of("type", view.getType()),
                    FlagInfo.of("click_type", clickType.name())
                }
            });
            return CheckResult.DENY;
        }
        jv(new Object[0]);
        return CheckResult.ALLOW;
    }
}
