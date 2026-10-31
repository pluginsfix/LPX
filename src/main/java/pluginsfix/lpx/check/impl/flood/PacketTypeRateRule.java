package pluginsfix.lpx.check.impl.flood;
import pluginsfix.lpx.api.check.RepeatedIntervalCounter;
final class PacketTypeRateRule {
    private final RepeatedIntervalCounter counter;
    private final boolean countFailedOnly;
    PacketTypeRateRule(RepeatedIntervalCounter counter, boolean countFailedOnly) {
        this.counter = counter;
        this.countFailedOnly = countFailedOnly;
    }
    public RepeatedIntervalCounter getCounter() {
        return counter;
    }
    public boolean isCountFailedOnly() {
        return countFailedOnly;
    }
    public RepeatedIntervalCounter s5(Object[] ignored) {
        return getCounter();
    }
    public boolean s1(Object[] ignored) {
        return isCountFailedOnly();
    }
}
