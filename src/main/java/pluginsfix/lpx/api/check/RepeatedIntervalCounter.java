package pluginsfix.lpx.api.check;
import java.util.Arrays;
public class RepeatedIntervalCounter extends IntervalCounter {
    private static final int DEFAULT_INTERVAL_MS = 1000;
    private final int maxWarnings;
    private final boolean[] periodWarnings;
    private int period;
    public RepeatedIntervalCounter(int maxCounter, int interval, int periods, int warnings) {
        super(maxCounter, interval);
        this.maxWarnings = warnings;
        this.periodWarnings = new boolean[Math.max(1, periods)];
    }
    public RepeatedIntervalCounter(int maxCounter, int periods, int warnings) {
        this(maxCounter, DEFAULT_INTERVAL_MS, periods, warnings);
    }
    @Override
    public boolean increment(double amount) {
        if (maxCounter < 0L) {
            return false;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;
        boolean triggered = false;
        if (elapsed >= interval) {
            int periodsPassed = (int) (elapsed / interval);
            triggered = advancePeriods(periodsPassed);
            startTime = now;
        }
        counter += amount;
        return triggered;
    }
    private boolean advancePeriods(int periodsPassed) {
        if (periodsPassed >= periodWarnings.length) {
            resetWarnings();
            period = 0;
            counter = 0.0D;
            return false;
        }
        for (int i = 0; i < periodsPassed; i++) {
            if (counter > maxCounter) {
                periodWarnings[period] = true;
            }
            int toClear = period - (periodWarnings.length - 1);
            if (toClear < 0) {
                toClear += periodWarnings.length;
            }
            periodWarnings[toClear] = false;
            period++;
            if (period > periodWarnings.length - 1) {
                period = 0;
            }
            counter = 0.0D;
        }
        if (getWarnings() >= maxWarnings) {
            resetWarnings();
            return true;
        }
        return false;
    }
    public int getPeriod() {
        return period;
    }
    public int getWarnings() {
        int warnings = 0;
        for (boolean warning : periodWarnings) {
            if (warning) {
                warnings++;
            }
        }
        return warnings;
    }
    public int getMaxPeriods() {
        return periodWarnings.length;
    }
    public int getMaxWarnings() {
        return maxWarnings;
    }
    public void resetWarnings() {
        Arrays.fill(periodWarnings, false);
    }
    private static RuntimeException b(RuntimeException runtimeException) {
        return runtimeException;
    }
}
