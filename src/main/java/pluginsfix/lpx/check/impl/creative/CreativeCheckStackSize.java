package pluginsfix.lpx.check.impl.creative;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
public class CreativeCheckStackSize extends AbstractCheckT<WrapperPlayClientCreativeInventoryAction> {
    private int l;
    public CreativeCheckStackSize(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        l = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 64);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        ItemStack itemStack = packet.getItemStack();
        if (itemStack == null || itemStack.isEmpty() || itemStack.getType() == ItemTypes.AIR) {
            return CheckResult.ALLOW;
        }
        int amount = itemStack.getAmount();
        if (amount > 0 && amount <= l) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            packet,
            0L,
            new FlagInfo[]{FlagInfo.of("amount", amount)}
        });
        return CheckResult.DENY;
    }
    public CheckResult sw(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        return Bu(new Object[]{0L, player, packet});
    }
}
