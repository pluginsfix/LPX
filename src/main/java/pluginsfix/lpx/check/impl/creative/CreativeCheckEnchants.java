package pluginsfix.lpx.check.impl.creative;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
public class CreativeCheckEnchants extends AbstractCheckT<WrapperPlayClientCreativeInventoryAction> {
    private int q;
    private int J;
    public CreativeCheckEnchants(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        q = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 127);
        J = j_(new Object[0]).getInt(kD(new Object[]{"min", 0L}), 0);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        ItemStack itemStack = packet.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        for (Enchantment enchantment : itemStack.getEnchantments(player.getUser().getClientVersion())) {
            int level = enchantment.getLevel();
            if (level > q || level < J) {
                AZ(new Object[]{
                    packet,
                    0L,
                    new FlagInfo[]{
                        FlagInfo.of("enchant", enchantment.getType().getName()),
                        FlagInfo.of("level", level)
                    }
                });
                return CheckResult.DENY;
            }
        }
        return CheckResult.ALLOW;
    }
    public CheckResult m9(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        WrapperPlayClientCreativeInventoryAction packet = (WrapperPlayClientCreativeInventoryAction) args[2];
        return Bu(new Object[]{0L, player, packet});
    }
}
