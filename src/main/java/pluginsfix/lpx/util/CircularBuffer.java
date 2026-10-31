package pluginsfix.lpx.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
public class CircularBuffer {
    private static final int SCALE = 4;
    private final BigDecimal[] Q;
    private final int r;
    private int F;
    private int x;
    public CircularBuffer(long ignored, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than zero");
        }
        this.r = size;
        this.Q = new BigDecimal[size];
        this.F = 0;
        this.x = 0;
    }
    public void lW(Object[] args) {
        BigDecimal value = (BigDecimal) args[1];
        Q[F] = value;
        F = (F + 1) % r;
        if (x < r) {
            x++;
        }
    }
    public double BC(Object[] args) {
        if (x == 0) {
            return 0.0D;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < x; i++) {
            if (Q[i] != null) {
                sum = sum.add(Q[i]);
            }
        }
        return sum.divide(new BigDecimal(x), SCALE, RoundingMode.HALF_UP).doubleValue();
    }
    public BigDecimal Bx(Object[] args) {
        if (x == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal min = null;
        for (int i = 0; i < x; i++) {
            BigDecimal value = Q[i];
            if (value == null) {
                continue;
            }
            if (min == null || value.compareTo(min) < 0) {
                min = value;
            }
        }
        return min == null ? BigDecimal.ZERO : min;
    }
    public BigDecimal B2(Object[] args) {
        if (x == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal max = null;
        for (int i = 0; i < x; i++) {
            BigDecimal value = Q[i];
            if (value == null) {
                continue;
            }
            if (max == null || value.compareTo(max) > 0) {
                max = value;
            }
        }
        return max == null ? BigDecimal.ZERO : max;
    }
    public BigDecimal GB(Object[] args) {
        double percentile = (Double) args[0];
        if (x == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal[] snapshot = new BigDecimal[x];
        int count = 0;
        for (int i = 0; i < x; i++) {
            if (Q[i] != null) {
                snapshot[count++] = Q[i];
            }
        }
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        snapshot = Arrays.copyOf(snapshot, count);
        Arrays.sort(snapshot);
        int index = (int) Math.ceil(percentile / 100.0D * count) - 1;
        index = Math.max(0, Math.min(index, count - 1));
        return snapshot[index];
    }
    public int Bo(Object[] args) {
        return x;
    }
    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}
