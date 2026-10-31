package pluginsfix.lpx.check.impl.book;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.IntervalCounter;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
public class BookCheckRate extends AbstractCheckT<WrapperPlayClientEditBook> {
    private long t;
    private int W;
    private IntervalCounter d;
    public BookCheckRate(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        W = j_(new Object[0]).getInt(kD(new Object[]{"min-delay-ms", 0L}), 50);
        int maxPerInterval = j_(new Object[0]).getInt(kD(new Object[]{"max-per-interval", 0L}), 8);
        int intervalMs = j_(new Object[0]).getInt(kD(new Object[]{"interval-ms", 0L}), 1000);
        d = new IntervalCounter(maxPerInterval, intervalMs);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return c5(args);
    }
    protected CheckResult c5(Object[] args) {
        WrapperPlayClientEditBook packet = (WrapperPlayClientEditBook) args[2];
        long now = System.currentTimeMillis();
        if (d.increment()) {
            AZ(
                packet,
                0L,
                new FlagInfo[]{FlagInfo.of("max-per-interval", d.getMaxCounter())}
            );
            t = now;
            return CheckResult.DENY;
        }
        long elapsed = now - t;
        t = now;
        if (elapsed < W) {
            AZ(
                packet,
                0L,
                new FlagInfo[]{
                    FlagInfo.of("elapsed-ms", elapsed),
                    FlagInfo.of("min-delay-ms", W)
                }
            );
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
