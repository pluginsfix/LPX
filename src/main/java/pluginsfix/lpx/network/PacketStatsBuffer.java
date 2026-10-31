package pluginsfix.lpx.network;
import pluginsfix.lpx.util.CircularBuffer;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Set;
import java.util.UUID;
public class PacketStatsBuffer {
    private final ConcurrentLinkedQueue<BigDecimal> D;
    private final Set<UUID> b;
    private final CircularBuffer n;
    private final CircularBuffer j;
    private final CircularBuffer Y;
    public PacketStatsBuffer(int baseSize, long ignored) {
        this.D = new ConcurrentLinkedQueue<>();
        this.b = ConcurrentHashMap.newKeySet();
        this.n = new CircularBuffer(0L, Math.max(1, baseSize * 3));
        this.j = new CircularBuffer(0L, Math.max(1, baseSize * 2));
        this.Y = new CircularBuffer(0L, Math.max(1, baseSize * 2));
    }
    public void eS(Object[] args) {
        long value = (Long) args[0];
        D.add(BigDecimal.valueOf(value));
    }
    public void NO(Object[] args) {
        UUID uuid = (UUID) args[0];
        b.add(uuid);
    }
    public void Nv(Object[] args) {
        UUID uuid = (UUID) args[0];
        b.remove(uuid);
    }
    public int zc(Object[] args) {
        return b.size();
    }
    public ConcurrentLinkedQueue<BigDecimal> zC(Object[] args) {
        return D;
    }
    public void i2(Object[] args) {
        BigDecimal primary = (BigDecimal) args[0];
        long token = (Long) args[1];
        BigDecimal secondary = (BigDecimal) args[2];
        BigDecimal tertiary = (BigDecimal) args[3];
        n.lW(new Object[]{token, primary});
        j.lW(new Object[]{token, secondary != null ? secondary : primary});
        Y.lW(new Object[]{token, tertiary != null ? tertiary : primary});
    }
    public BigDecimal zd(Object[] args) {
        long token = (Long) args[0];
        return n.GB(new Object[]{10.0D, token});
    }
    public BigDecimal z0(Object[] args) {
        long token = (Long) args[0];
        return n.GB(new Object[]{90.0D, token});
    }
    public CircularBuffer zD(Object[] args) {
        return n;
    }
}
