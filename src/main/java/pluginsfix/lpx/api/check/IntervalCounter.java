package pluginsfix.lpx.api.check;
public class IntervalCounter {
    private static final long DEFAULT_INTERVAL_MS = 1000L;
    protected final long maxCounter;
    protected final long interval;
    protected double counter;
    protected long startTime;
    protected long lastFlag;
    public IntervalCounter(long maxCounter) {
        this(maxCounter, DEFAULT_INTERVAL_MS);
    }
    public IntervalCounter(long maxCounter, long interval) {
        this.maxCounter = maxCounter;
        this.interval = interval;
        this.startTime = System.currentTimeMillis();
        this.lastFlag = 0L;
    }
    public boolean increment() {
        return increment(1.0D);
    }
    public boolean increment(double amount) {
        if (maxCounter < 0L) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (now - startTime >= interval) {
            reset();
        }
        counter += amount;
        if (counter > maxCounter) {
            lastFlag = now;
            reset();
            return true;
        }
        return false;
    }
    protected void reset() {
        counter = 0.0D;
        startTime = System.currentTimeMillis();
    }
    public long getInterval() {
        return interval;
    }
    public double getValue() {
        return counter;
    }
    public long getMaxCounter() {
        return maxCounter;
    }
    public long getStartTime() {
        return startTime;
    }
    public boolean hasFlaggedRecently() {
        return hasFlaggedRecently(interval);
    }
    public boolean hasFlaggedRecently(long durationMs) {
        return System.currentTimeMillis() - lastFlag < durationMs;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
