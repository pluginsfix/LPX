package pluginsfix.lpx.check.window;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import org.bukkit.entity.Player;
public class ClickWindowNullViewCheck extends AbstractCheckT<WrapperPlayClientClickWindow> {
    public ClickWindowNullViewCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkNullView(args);
    }
    public CheckResult checkNullView(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientClickWindow packet = (WrapperPlayClientClickWindow) args[2];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        InventoryViewAdapter view = protocolPlayer.lpx().getInventoryViewAdapter(bukkitPlayer);
        if (view != null && view.isNull()) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("slot", packet.getSlot())}
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
}
