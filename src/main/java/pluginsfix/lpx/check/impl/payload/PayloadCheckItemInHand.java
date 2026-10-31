package pluginsfix.lpx.check.impl.payload;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.manager.CheckManagerImpl;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class PayloadCheckItemInHand extends AbstractCheckT<WrapperPlayClientPluginMessage> {
    public PayloadCheckItemInHand(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return PV(args);
    }
    public CheckResult PV(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPluginMessage packet = (WrapperPlayClientPluginMessage) args[2];
        if (!CheckManagerImpl.L.contains(packet.getChannelName())) {
            return CheckResult.ALLOW;
        }
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        ItemStack mainHand = bukkitPlayer.getInventory().getItemInMainHand();
        ItemStack offHand = bukkitPlayer.getInventory().getItemInOffHand();
        if (isBook(mainHand) || isBook(offHand)) {
            return CheckResult.ALLOW;
        }
        String material = mainHand == null || mainHand.getType() == null
            ? "null"
            : mainHand.getType().name();
        AZ(new Object[]{
            packet,
            token,
            new FlagInfo[]{FlagInfo.of("material", material)}
        });
        return CheckResult.DENY;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private boolean isBook(ItemStack stack) {
        if (stack == null || stack.getType() == null) {
            return false;
        }
        String type = stack.getType().name();
        return type.contains("WRITABLE_BOOK") || type.contains("WRITTEN_BOOK");
    }
}
