package pluginsfix.lpx.check.impl.creative;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.impl.item.ItemNbtUtils;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
public class CreativeCheckNBTList extends AbstractCheckT<WrapperPlayClientCreativeInventoryAction> {
    private int l;
    public CreativeCheckNBTList(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        l = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 3000);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        ItemStack itemStack = packet.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        NBTList<NBTString> list = ItemNbtUtils.bz(new Object[]{0L, itemStack}).orElse(null);
        if (list == null || list.isEmpty()) {
            return CheckResult.ALLOW;
        }
        int totalLength = 0;
        for (NBTString value : list.getTags()) {
            totalLength += value.getValue().length();
        }
        if (totalLength <= l) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            packet,
            0L,
            new FlagInfo[]{
                FlagInfo.of("length", totalLength)
            }
        });
        return CheckResult.DENY;
    }
    public CheckResult zo(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        return Bu(new Object[]{0L, player, packet});
    }
}
