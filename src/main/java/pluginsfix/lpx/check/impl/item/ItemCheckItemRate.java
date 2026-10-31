package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.check.RepeatedIntervalCounter;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
public class ItemCheckItemRate extends AbstractCheckT<ItemStackDecodeEvent> {
    private RepeatedIntervalCounter F;
    private long o;
    private static final long j = 1024L;
    public ItemCheckItemRate(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        int max = readInt("max", "rate-max", 50);
        int interval = readInt("interval", "rate-interval", 1000);
        int periods = readInt("periods", "max-periods", 5);
        int warnings = readInt("warnings", "max-warnings", 2);
        int divisor = readInt("size-divisor", "packet-size-divisor", (int) j);
        o = j_(new Object[0]).getLong(kD(new Object[]{"size-multiplier", 0L}), divisor);
        F = new RepeatedIntervalCounter(max, interval, periods, warnings);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return Og(new Object[]{player, token, event});
    }
    protected CheckResult Og(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        long token = (Long) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        if (player.getPrinterData().isUsingPrinter()) {
            return CheckResult.ALLOW;
        }
        ReceivedPacket lastPacket = player.getLastReceivedPacket();
        long packetSize = lastPacket != null ? lastPacket.getSize() : 0L;
        double divisor = Math.max(1.0D, o);
        double weight = packetSize / divisor / 4.0D;
        if (weight <= 0.0D) {
            weight = 1.0D;
        }
        if (ItemNbtUtils.bt(new Object[]{event.getItemStack(), token})) {
            weight *= 0.15D;
        }
        if (ItemNbtUtils.uo(new Object[]{token, player})) {
            weight *= 1.25D;
        }
        if (ItemNbtUtils.uB(new Object[]{player, token})) {
            weight *= 0.5D;
        }
        if (!F.increment(weight)) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("rate", F.getMaxCounter()),
                FlagInfo.of("weight", weight)
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
