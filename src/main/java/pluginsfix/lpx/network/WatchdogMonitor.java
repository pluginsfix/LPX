package pluginsfix.lpx.network;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import pluginsfix.lpx.config.ConfigManager;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
public class WatchdogMonitor implements Listener {
    private final LPXCore core;
    private final ConfigManager configManager;
    private final Map<Long, ChannelEntry> channelsByThreadId;
    private final AtomicBoolean running;
    private ScheduledExecutorService executor;
    private static boolean legacyState;
    public WatchdogMonitor(long ignored, LPXCore core) {
        this.core = core;
        this.configManager = core.sZ(new Object[0]);
        this.channelsByThreadId = new HashMap<>();
        this.running = new AtomicBoolean(false);
    }
    public synchronized void tN(Object[] ignored) {
        if (running.get()) {
            to(new Object[]{0, 0, 0});
            return;
        }
        startMonitor();
    }
    private void startMonitor() {
        if (running.get()) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor(
            new ThreadFactoryBuilder()
                .setNameFormat("lpx-netty-watchdog-%d")
                .setDaemon(true)
                .build()
        );
        long checkFrequencyMs = configManager.BJ(new Object[0]).Xi(new Object[0]).vq(new Object[0]);
        executor.scheduleAtFixedRate(this::checkThreads, checkFrequencyMs, checkFrequencyMs, TimeUnit.MILLISECONDS);
        core.getPlugin().getServer().getPluginManager().registerEvents(this, (Plugin) core.getPlugin());
        running.set(true);
    }
    public synchronized void to(Object[] ignored) {
        if (!running.get()) {
            return;
        }
        HandlerList.unregisterAll(this);
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(3L, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException interruptedException) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        running.set(false);
    }
    public boolean tW(Object[] ignored) {
        return running.get();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        tX(new Object[]{event.getPlayer(), 0L});
    }
    private void tX(Object[] args) {
        Player player = (Player) args[0];
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) {
            return;
        }
        Object rawChannel = user.getChannel();
        Channel channel = rawChannel instanceof Channel ? (Channel) rawChannel : null;
        if (channel == null) {
            return;
        }
        if (!player.hasMetadata("NPC")) {
            e_(new Object[]{channel, 0L});
        }
    }
    public void e_(Object[] args) {
        Channel channel = (Channel) args[0];
        if (!running.get() || executor == null) {
            return;
        }
        channel.eventLoop().submit(() -> {
            long threadId = Thread.currentThread().getId();
            String threadName = Thread.currentThread().getName();
            executor.execute(() -> {
                channelsByThreadId.computeIfAbsent(threadId, id -> {
                    ChannelEntry entry = new ChannelEntry(threadName);
                    entry.l2(new Object[]{System.currentTimeMillis()});
                    entry.lD(new Object[]{0L});
                    channel.eventLoop().scheduleAtFixedRate(
                        () -> entry.l2(new Object[]{System.currentTimeMillis()}),
                        1L,
                        1L,
                        TimeUnit.MILLISECONDS
                    );
                    return entry;
                });
            });
        });
    }
    private void checkThreads() {
        if (!running.get()) {
            return;
        }
        long now = System.currentTimeMillis();
        long maxBlockedMs = configManager.BJ(new Object[0]).Xi(new Object[0]).ve(new Object[0]);
        long reportCooldownMs = configManager.BJ(new Object[0]).Xi(new Object[0]).va(new Object[0]);
        for (Map.Entry<Long, ChannelEntry> entry : channelsByThreadId.entrySet()) {
            ChannelEntry channelEntry = entry.getValue();
            long threadId = entry.getKey();
            long blockedFor = now - channelEntry.B3(new Object[0]);
            long cooldownPassed = now - channelEntry.Bc(new Object[0]);
            if (blockedFor <= maxBlockedMs || cooldownPassed <= reportCooldownMs) {
                continue;
            }
            PacketLogger logger = core.sY(new Object[0]);
            logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () ->
                "Watchdog: thread '" + channelEntry.B4(new Object[0]) + "' blocked for " + blockedFor + "ms"
            });
            ThreadInfo[] infos = ManagementFactory.getThreadMXBean().getThreadInfo(new long[]{threadId}, true, true);
            ThreadInfo threadInfo = infos.length == 0 ? null : infos[0];
            if (threadInfo != null) {
                logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () ->
                    "State=" + threadInfo.getThreadState()
                        + ", native=" + threadInfo.isInNative()
                        + ", suspended=" + threadInfo.isSuspended()
                });
                dumpMonitors(logger, threadInfo);
                dumpStack(logger, threadInfo);
            }
            channelEntry.lD(new Object[]{now});
        }
    }
    private void dumpMonitors(PacketLogger logger, ThreadInfo info) {
        MonitorInfo[] monitors = info.getLockedMonitors();
        if (monitors.length > 0) {
            logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () -> "Locked monitors:"});
            for (MonitorInfo monitor : monitors) {
                logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () -> " - " + monitor});
            }
        }
        LockInfo[] synchronizers = info.getLockedSynchronizers();
        if (synchronizers.length > 0) {
            logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () -> "Locked synchronizers:"});
            for (LockInfo lockInfo : synchronizers) {
                logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () -> " - " + lockInfo});
            }
        }
    }
    private void dumpStack(PacketLogger logger, ThreadInfo info) {
        logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () -> "Stack trace:"});
        for (StackTraceElement element : info.getStackTrace()) {
            logger.Lv(new Object[]{0L, (java.util.function.Supplier<String>) () -> "  at " + element});
        }
    }
    public static void Lj(boolean value) {
        legacyState = value;
    }
    public static boolean tV() {
        return legacyState;
    }
    public static boolean tJ() {
        return !legacyState;
    }
}
