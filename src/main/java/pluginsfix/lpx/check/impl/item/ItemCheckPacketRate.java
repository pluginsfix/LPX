package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.IntervalCounter;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
public class ItemCheckPacketRate extends AbstractCheckT<ItemStackDecodeEvent> {
    private int V;
    private IntervalCounter A;
    public ItemCheckPacketRate(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        V = readInt("size-threshold", "packet-size-threshold", 4096);
        int max = readInt("max", "rate-max", 80);
        int interval = readInt("interval", "rate-interval", 1000);
        A = new IntervalCounter(max, interval);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        long token = (Long) args[0];
        return _V(new Object[]{player, event, token});
    }
    protected CheckResult _V(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[1];
        long token = (Long) args[2];
        ReceivedPacket lastPacket = player.getLastReceivedPacket();
        long packetSize = lastPacket != null ? lastPacket.getSize() : 0L;
        if (packetSize <= V) {
            return CheckResult.ALLOW;
        }
        if (!A.increment()) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("rate", A.getMaxCounter()),
                FlagInfo.of("size", packetSize)
            }
        });
        return Q.getVl() >= Q.getCheckOptions().getMaxVL() ? CheckResult.DENY : CheckResult.ALLOW;
    }
    private int readInt(String primary, String legacy, int fallback) {
        String primaryPath = kD(new Object[]{primary, 0L});
        if (j_(new Object[0]).contains(primaryPath)) {
            return j_(new Object[0]).getInt(primaryPath, fallback);
        }
        return j_(new Object[0]).getInt(kD(new Object[]{legacy, 0L}), fallback);
    }
}
