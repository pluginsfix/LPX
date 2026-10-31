package pluginsfix.lpx.check.impl.flood;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.IntervalCounter;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
public class FloodCheckPacketRate extends AbstractCheckT<PacketReceiveEvent> {
    private IntervalCounter counter;
    public FloodCheckPacketRate(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        int max = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 1100);
        counter = new IntervalCounter(max);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return Lr(new Object[]{args[1], args[2], args[0]});
    }
    public CheckResult Lr(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        PacketReceiveEvent event = (PacketReceiveEvent) args[1];
        if (player.getPrinterData().isUsingPrinter()) {
            return CheckResult.ALLOW;
        }
        if (!counter.increment()) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            0L,
            new FlagInfo[]{
                FlagInfo.of("max", counter.getMaxCounter())
            }
        });
        return CheckResult.DENY;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
