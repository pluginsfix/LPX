package pluginsfix.lpx.check.impl.creative;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.impl.item.ItemNbtUtils;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
public class CreativeCheckBedrock extends AbstractCheckT<WrapperPlayClientCreativeInventoryAction> {
    private int P;
    public CreativeCheckBedrock(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        P = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 16384);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        ItemStack itemStack = packet.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        String value = ItemNbtUtils.bF(new Object[]{0, itemStack, 0, 0}).orElse(null);
        if (value == null) {
            return CheckResult.ALLOW;
        }
        int length = value.length();
        if (length <= P) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            packet,
            0L,
            new FlagInfo[]{
                FlagInfo.of("length", length),
                FlagInfo.of("value", value)
            }
        });
        return CheckResult.DENY;
    }
    public CheckResult dt(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        return Bu(new Object[]{0L, player, packet});
    }
}
