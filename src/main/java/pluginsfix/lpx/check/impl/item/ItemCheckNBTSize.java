package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.IntervalCounter;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
public class ItemCheckNBTSize extends AbstractCheckT<ItemStackDecodeEvent> {
    private IntervalCounter S;
    private int l;
    private IntervalCounter N;
    public ItemCheckNBTSize(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        S = new IntervalCounter(readInt("book-rate-max", "rate-max", 40));
        l = readInt("max-packet-size", "size-threshold", 8192);
        N = new IntervalCounter(readInt("size-rate-max", "violation-rate-max", 8));
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return j8(new Object[]{token, player, event});
    }
    protected CheckResult j8(Object[] args) {
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null || itemStack.getType() != ItemTypes.WRITABLE_BOOK) {
            return CheckResult.ALLOW;
        }
        if (S.increment()) {
            AZ(new Object[]{
                event,
                token,
                new FlagInfo[]{
                    FlagInfo.of("rate", S.getMaxCounter())
                }
            });
            return CheckResult.DENY;
        }
        NBTCompound nbt = itemStack.getNBT();
        if (nbt == null || !nbt.getTags().containsKey("pages")) {
            return CheckResult.ALLOW;
        }
        ReceivedPacket lastPacket = F.getLastReceivedPacket();
        long packetSize = lastPacket != null ? lastPacket.getSize() : 0L;
        if (packetSize <= l) {
            return CheckResult.ALLOW;
        }
        if (!N.increment()) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("rate", N.getMaxCounter()),
                FlagInfo.of("size", packetSize),
                FlagInfo.of("limit", l)
            }
        });
        return CheckResult.DENY;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private int readInt(String primary, String legacy, int fallback) {
        String primaryPath = kD(new Object[]{primary, 0L});
        if (j_(new Object[0]).contains(primaryPath)) {
            return j_(new Object[0]).getInt(primaryPath, fallback);
        }
        return j_(new Object[0]).getInt(kD(new Object[]{legacy, 0L}), fallback);
    }
}
