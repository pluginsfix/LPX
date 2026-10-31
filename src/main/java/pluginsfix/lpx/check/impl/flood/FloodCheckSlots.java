package pluginsfix.lpx.check.impl.flood;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.check.RepeatedIntervalCounter;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.impl.item.ItemNbtUtils;
import com.github.retrooper.packetevents.event.simple.ReadSlotsEvent;
public class FloodCheckSlots extends AbstractCheckT<ReadSlotsEvent> {
    private RepeatedIntervalCounter counter;
    private boolean cancelPackets;
    public FloodCheckSlots(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        int max = readInt("max", 70);
        int interval = readIntEither("interval", "reset-interval", 250);
        int periods = readIntEither("periods", "max-periods", 5);
        int warnings = readIntEither("warnings", "max-warnings", 2);
        this.cancelPackets = j_(new Object[0]).getBoolean(kD(new Object[]{"cancel", 0L}), false);
        this.counter = new RepeatedIntervalCounter(max, interval, periods, warnings);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return U6(args);
    }
    protected CheckResult U6(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        ReadSlotsEvent event = (ReadSlotsEvent) args[2];
        double weight = event.getSlotsCount();
        if (ItemNbtUtils.uo(new Object[]{0L, player})) {
            weight *= 1.25D;
        } else if (ItemNbtUtils.uB(new Object[]{player, 0L})) {
            weight *= 0.5D;
        }
        if (counter.increment(weight)) {
            AZ(new Object[]{
                event,
                0L,
                new FlagInfo[]{
                    FlagInfo.of("max", counter.getMaxCounter()),
                    FlagInfo.of("interval", counter.getInterval() + "ms"),
                    FlagInfo.of("slots", event.getSlotsCount())
                }
            });
            return cancelPackets ? CheckResult.DENY : CheckResult.ALLOW;
        }
        if (cancelPackets && counter.hasFlaggedRecently(counter.getInterval())) {
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private int readInt(String key, int fallback) {
        return j_(new Object[0]).getInt(kD(new Object[]{key, 0L}), fallback);
    }
    private int readIntEither(String primaryKey, String legacyKey, int fallback) {
        String primaryPath = kD(new Object[]{primaryKey, 0L});
        if (j_(new Object[0]).contains(primaryPath)) {
            return j_(new Object[0]).getInt(primaryPath, fallback);
        }
        return j_(new Object[0]).getInt(kD(new Object[]{legacyKey, 0L}), fallback);
    }
}
