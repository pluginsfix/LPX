package pluginsfix.lpx.network;

import io.netty.channel.EventLoop;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.listeners.impl.TickListener;
import pluginsfix.lpx.tick.TickSource;
import pluginsfix.lpx.tick.TickSourcePaper;
import pluginsfix.lpx.tick.TickSourceSpigot;
import pluginsfix.lpx.util.CircularBuffer;
import pluginsfix.lpx.util.TpsParser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkStatsCollector {
    private static final String PAPER_TICK_EVENT_CLASS = "com.destroystokyo.paper.event.server.ServerTickStartEvent";
    private static final int BUFFER_SIZE = 300;
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private boolean enabled;
    private final CircularBuffer globalAverageBuffer;
    private final CircularBuffer globalMedianBuffer;
    private final CircularBuffer globalP95Buffer;
    private final CircularBuffer loopAverageBuffer;
    private final CircularBuffer loopMedianBuffer;
    private final CircularBuffer loopMaxBuffer;
    private final CircularBuffer globalMaxBuffer;
    private final Map<EventLoop, Integer> loopIds;
    private final Map<EventLoop, PacketStatsBuffer> loopStats;
    private final LPXCore core;
    private final TickSource tickSource;
    private static int[] legacyState;

    public NetworkStatsCollector(LPXCore core, long ignored) {
        this.enabled = false;
        this.globalAverageBuffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.globalMedianBuffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.globalP95Buffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.loopAverageBuffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.loopMedianBuffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.loopMaxBuffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.globalMaxBuffer = new CircularBuffer(0L, BUFFER_SIZE);
        this.loopIds = new ConcurrentHashMap<>();
        this.loopStats = new ConcurrentHashMap<>();
        this.core = core;
        this.tickSource = createTickSource();
        this.tickSource.addListener((TickListener) tick -> collectTickSample());
    }

    public void start() {
        if (enabled) {
            return;
        }
        enabled = true;
        tickSource.start();
    }

    public void stop() {
        if (!enabled) {
            return;
        }
        tickSource.close();
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void recordPacketProcessingNanos(long value, EventLoop eventLoop) {
        if (!enabled) {
            return;
        }
        ensureStatsBuffer(Thread.currentThread(), eventLoop).eS(new Object[]{value});
    }

    public void registerPlayerLoop(UUID playerId, Thread thread, EventLoop eventLoop) {
        ensureStatsBuffer(thread, eventLoop).NO(new Object[]{playerId});
    }

    public void unregisterPlayerLoop(UUID playerId, Thread thread, EventLoop eventLoop) {
        ensureStatsBuffer(thread, eventLoop).Nv(new Object[]{playerId});
    }

    public void collectTickSample() {
        if (!enabled) {
            return;
        }

        List<BigDecimal> loopAverages = new ArrayList<>();
        List<BigDecimal> loopMedians = new ArrayList<>();
        List<BigDecimal> loopP95Values = new ArrayList<>();
        List<BigDecimal> loopMaximums = new ArrayList<>();

        for (PacketStatsBuffer statsBuffer : loopStats.values()) {
            ConcurrentLinkedQueue<BigDecimal> queue = statsBuffer.zC(new Object[0]);
            List<BigDecimal> drained = drainQueue(queue);
            if (drained.isEmpty()) {
                continue;
            }

            BigDecimal avg = average(drained);
            BigDecimal p50 = percentile(drained, 50.0);
            BigDecimal p95 = percentile(drained, 95.0);
            BigDecimal max = max(drained);

            statsBuffer.i2(new Object[]{avg, 0L, p50, p95});

            loopAverages.add(avg);
            loopMedians.add(p50);
            loopP95Values.add(p95);
            loopMaximums.add(max);
        }

        if (loopAverages.isEmpty()) {
            return;
        }

        BigDecimal globalAvg = average(loopAverages);
        BigDecimal globalMedian = average(loopMedians);
        BigDecimal globalP95 = average(loopP95Values);
        BigDecimal globalMax = max(loopMaximums);

        addSample(globalAverageBuffer, globalAvg);
        addSample(globalMedianBuffer, globalMedian);
        addSample(globalP95Buffer, globalP95);
        addSample(loopAverageBuffer, average(loopAverages));
        addSample(loopMedianBuffer, average(loopMedians));
        addSample(loopMaxBuffer, globalMax);
        addSample(globalMaxBuffer, globalMax);
    }

    public BigDecimal getGlobalAverageP50() {
        return globalAverageBuffer.GB(new Object[]{50.0, 0L});
    }

    public BigDecimal getGlobalMedianP50() {
        return globalMedianBuffer.GB(new Object[]{50.0, 0L});
    }

    public BigDecimal getGlobalP95P50() {
        return globalP95Buffer.GB(new Object[]{50.0, 0L});
    }

    public BigDecimal getLoopAverageP50() {
        return loopAverageBuffer.GB(new Object[]{50.0, 0L});
    }

    public BigDecimal getLoopMedianP50() {
        return globalMedianBuffer.GB(new Object[]{50.0, 0L});
    }

    public BigDecimal getLoopP95P50() {
        return globalP95Buffer.GB(new Object[]{50.0, 0L});
    }

    public BigDecimal getGlobalAverageP10() {
        return globalAverageBuffer.GB(new Object[]{10.0, 0L});
    }

    public BigDecimal getGlobalAverageP90() {
        return globalAverageBuffer.GB(new Object[]{90.0, 0L});
    }

    public BigDecimal getLoopAverageP95() {
        return loopAverageBuffer.GB(new Object[]{95.0, 0L});
    }

    public BigDecimal getLoopMedianP95() {
        return loopMedianBuffer.GB(new Object[]{95.0, 0L});
    }

    public BigDecimal getGlobalP95P95() {
        return globalP95Buffer.GB(new Object[]{95.0, 0L});
    }

    public BigDecimal getLoopMax() {
        return loopMaxBuffer.B2(new Object[]{0L});
    }

    public BigDecimal getGlobalMax() {
        return globalMaxBuffer.B2(new Object[]{0L});
    }

    public BigDecimal getGlobalP95Max() {
        return globalP95Buffer.B2(new Object[]{0L});
    }

    public int getCurrentTick() {
        return tickSource.getCurrentTick();
    }

    public Map<Integer, MetricSample> getLoopMetrics() {
        Map<Integer, MetricSample> result = new HashMap<>();
        for (Map.Entry<EventLoop, Integer> entry : loopIds.entrySet()) {
            PacketStatsBuffer stats = loopStats.get(entry.getKey());
            if (stats == null) {
                continue;
            }
            result.put(entry.getValue(), new MetricSample(stats.z0(new Object[]{0L}), stats.zc(new Object[0])));
        }
        return result;
    }

    public static void setLegacyState(int[] value) {
        legacyState = value;
    }

    public static int[] getLegacyState() {
        return legacyState;
    }

    public void nW(Object[] ignored) {
        start();
    }

    public void nz(Object[] ignored) {
        stop();
    }

    public boolean nG(Object[] ignored) {
        return isEnabled();
    }

    public void h_(Object[] args) {
        recordPacketProcessingNanos((Long) args[0], (EventLoop) args[2]);
    }

    public void kN(Object[] args) {
        registerPlayerLoop((UUID) args[0], (Thread) args[1], (EventLoop) args[2]);
    }

    public void kO(Object[] args) {
        unregisterPlayerLoop((UUID) args[0], (Thread) args[1], (EventLoop) args[2]);
    }

    public void n8(Object[] ignored) {
        collectTickSample();
    }

    public BigDecimal nP(Object[] ignored) {
        return getGlobalAverageP50();
    }

    public BigDecimal nv(Object[] ignored) {
        return getGlobalMedianP50();
    }

    public BigDecimal nH(Object[] ignored) {
        return getGlobalP95P50();
    }

    public BigDecimal nD(Object[] ignored) {
        return getLoopAverageP50();
    }

    public BigDecimal ns(Object[] ignored) {
        return getLoopMedianP50();
    }

    public BigDecimal nU(Object[] ignored) {
        return getLoopP95P50();
    }

    public BigDecimal nc(Object[] ignored) {
        return getGlobalAverageP10();
    }

    public BigDecimal ni(Object[] ignored) {
        return getGlobalAverageP90();
    }

    public BigDecimal nC(Object[] ignored) {
        return getLoopAverageP95();
    }

    public BigDecimal nK(Object[] ignored) {
        return getLoopMedianP95();
    }

    public BigDecimal nf(Object[] ignored) {
        return getGlobalP95P95();
    }

    public BigDecimal nI(Object[] ignored) {
        return getLoopMax();
    }

    public BigDecimal nd(Object[] ignored) {
        return getGlobalMax();
    }

    public BigDecimal n_(Object[] ignored) {
        return getGlobalP95Max();
    }

    public int ny(Object[] ignored) {
        return getCurrentTick();
    }

    public Map<Integer, MetricSample> nS(Object[] ignored) {
        return getLoopMetrics();
    }

    public static void bj(int[] value) {
        setLegacyState(value);
    }

    public static int[] nV() {
        return getLegacyState();
    }

    private PacketStatsBuffer ensureStatsBuffer(Thread thread, EventLoop eventLoop) {
        loopIds.computeIfAbsent(eventLoop, key -> TpsParser.Zu(new Object[]{thread.getName()}));
        return loopStats.computeIfAbsent(eventLoop, key -> new PacketStatsBuffer(BUFFER_SIZE, 0L));
    }

    private TickSource createTickSource() {
        if (classExists(PAPER_TICK_EVENT_CLASS)) {
            return new TickSourcePaper(core.getPlugin());
        }
        return new TickSourceSpigot(core.getPlugin());
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static void addSample(CircularBuffer buffer, BigDecimal value) {
        buffer.lW(new Object[]{0L, value == null ? ZERO : value});
    }

    private static List<BigDecimal> drainQueue(ConcurrentLinkedQueue<BigDecimal> queue) {
        List<BigDecimal> drained = new ArrayList<>();
        BigDecimal value;
        while ((value = queue.poll()) != null) {
            drained.add(value);
        }
        return drained;
    }

    private static BigDecimal average(List<BigDecimal> values) {
        if (values.isEmpty()) {
            return ZERO;
        }
        BigDecimal sum = ZERO;
        for (BigDecimal value : values) {
            sum = sum.add(value);
        }
        return sum.divide(BigDecimal.valueOf(values.size()), 4, RoundingMode.HALF_UP);
    }

    private static BigDecimal percentile(List<BigDecimal> values, double percentile) {
        if (values.isEmpty()) {
            return ZERO;
        }
        List<BigDecimal> sorted = new ArrayList<>(values);
        sorted.sort(Comparator.naturalOrder());
        int index = (int) Math.ceil((percentile / 100.0D) * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }

    private static BigDecimal max(List<BigDecimal> values) {
        return values.isEmpty() ? ZERO : Collections.max(values);
    }
}
