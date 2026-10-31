package pluginsfix.lpx.network;

import java.math.BigDecimal;

public final class MetricSample {
    private final BigDecimal average;
    private final int count;

    public MetricSample(BigDecimal average, int count) {
        this.average = average;
        this.count = count;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal bL(Object[] ignored) {
        return getAverage();
    }

    public int bu(Object[] ignored) {
        return getCount();
    }
}
