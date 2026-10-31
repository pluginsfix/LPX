package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
public class ItemCheckBooleanNBT extends AbstractCheckT<ItemStackDecodeEvent> {
    private static final String RESOLVED_TAG = "resolved";
    public ItemCheckBooleanNBT(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return Iw(new Object[]{player, token, event});
    }
    public static boolean i_(Object[] args) {
        ItemStack itemStack = (ItemStack) args[1];
        if (itemStack == null) {
            return false;
        }
        boolean resolved = itemStack.getType() == ItemTypes.WRITTEN_BOOK;
        NBTCompound nbt = itemStack.getNBT();
        if (nbt == null || !nbt.getTags().containsKey(RESOLVED_TAG)) {
            return resolved;
        }
        return nbt.getBoolean(RESOLVED_TAG);
    }
    public CheckResult Iw(Object[] args) {
        long token = (Long) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        ItemType type = itemStack.getType();
        if (!i_(new Object[]{token, itemStack}) || type == ItemTypes.WRITTEN_BOOK) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("resolved", true),
                FlagInfo.of("material", type.getName())
            }
        });
        return CheckResult.DENY;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
