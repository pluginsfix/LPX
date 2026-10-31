package pluginsfix.lpx.network;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import pluginsfix.lpx.alert.LogTarget;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.core.LPXCore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
public class PacketLogger {
    private static final int QUEUE_SIZE = 4096;
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 3L;
    private final LPXCore core;
    private final ExecutorService executor;
    private static boolean legacyState;
    public PacketLogger(LPXCore core, char ignored1, int ignored2, char ignored3) {
        this.core = core;
        ThreadFactory factory = new ThreadFactoryBuilder()
            .setNameFormat("lpx-packet-logger-%d")
            .setDaemon(true)
            .build();
        this.executor = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(QUEUE_SIZE),
            factory,
            new ThreadPoolExecutor.DiscardPolicy()
        );
    }
    public void X3(Object[] args) {
        ReceivedPacket receivedPacket = (ReceivedPacket) args[0];
        Lv(new Object[]{0L, (Supplier<String>) () -> formatReceivedPacket(receivedPacket)});
    }
    public void Lv(Object[] args) {
        @SuppressWarnings("unchecked")
        Supplier<String> supplier = (Supplier<String>) args[1];
        if (executor.isShutdown()) {
            return;
        }
        LogTarget target = core.sZ(new Object[0]).BJ(new Object[0]).XJ(new Object[0]);
        if (target == LogTarget.CONSOLE || target == LogTarget.BOTH) {
            executor.execute(() -> core.getPlugin().getLogger().info(supplier.get()));
        }
        if (target == LogTarget.FILE || target == LogTarget.BOTH) {
            core.s1(new Object[0]).getFileLogger().l1(new Object[]{supplier});
        }
    }
    public void NU(Object[] ignored) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException interruptedException) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    public static void AO(boolean value) {
        legacyState = value;
    }
    public static boolean NY() {
        return legacyState;
    }
    public static boolean N0() {
        return !legacyState;
    }
    private String formatReceivedPacket(ReceivedPacket packet) {
        return packet.getPlayerName()
            + " "
            + packet.getPacketType().getName()
            + " "
            + packet.getFormattedElapsedTime()
            + " ms "
            + packet.getSize()
            + " bytes "
            + packet;
    }
}
