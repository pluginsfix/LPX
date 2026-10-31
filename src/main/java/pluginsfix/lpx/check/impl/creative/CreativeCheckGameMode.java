package pluginsfix.lpx.check.impl.creative;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
public class CreativeCheckGameMode extends AbstractCheckT<WrapperPlayClientCreativeInventoryAction> {
    public CreativeCheckGameMode(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        Player player = protocolPlayer.getPlayer().orElse(null);
        if (player == null) {
            return CheckResult.ALLOW;
        }
        boolean allowed = protocolPlayer.getPrinterData().isUsingPrinter() || player.getGameMode() == GameMode.CREATIVE;
        if (allowed) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{packet, 0L, new FlagInfo[0]});
        return CheckResult.DENY;
    }
    public CheckResult xh(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[1];
        return Bu(new Object[]{0L, player, packet});
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
