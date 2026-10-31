package pluginsfix.lpx.core;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.api.metrics.MetricsLite;
import pluginsfix.lpx.check.dispatch.CheckDispatcher;
import pluginsfix.lpx.command.CommandRouter;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import com.github.retrooper.packetevents.PacketEvents;
import pluginsfix.lpx.listeners.impl.BukkitEventListener;
import pluginsfix.lpx.listeners.impl.CommandHideListener;
import pluginsfix.lpx.listeners.impl.ContainerInteractListener;
import pluginsfix.lpx.listeners.impl.LoginListener;
import pluginsfix.lpx.listeners.impl.PreViaPacketListener;
import pluginsfix.lpx.listeners.impl.RedstoneListener;
import pluginsfix.lpx.network.PacketHandler;
import pluginsfix.lpx.tick.TickTask;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
public class PluginBootstrap {
    private static final long TICK_TO_MILLIS = 50L;
    private static boolean bootstrapped;
    public PluginBootstrap() {
    }
    public static void reset() {
        bootstrapped = false;
    }
    public void q(LPXCore core) {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        core.sZ(new Object[0]).nY(new Object[]{core.getPlugin(), 0L});
        core.s1(new Object[0]).flushAll();
        core.getCheckManager().registerChecks();
        core.sJ(new Object[0]).MN(new Object[0]);
        core.wb(new Object[]{0L, new Listener[]{new BukkitEventListener(core), new ContainerInteractListener(core)}});
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        if (version.is(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_13)
            && version.is(VersionComparison.OLDER_THAN, ServerVersion.V_1_19_4)) {
            core.oq(new Object[]{new RedstoneListener(core)});
        }
        if (version.is(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_13)) {
            core.oq(new Object[]{new CommandHideListener(core)});
        }
        if (version.is(VersionComparison.OLDER_THAN_OR_EQUALS, ServerVersion.V_1_8_8)) {
            core.oq(new Object[]{new LoginListener(core)});
        }
        new CommandRouter((short) 0, (short) 0, 0, core);
        if (core.sZ(new Object[0]).Bw(new Object[0]).Yp(new Object[0])) {
            FoliaScheduler.getAsyncScheduler().runAtFixedRate(
                core.getPlugin(),
                task -> new TickTask(0L, core).run(),
                1L,
                (long) core.sZ(new Object[0]).Bw(new Object[0]).Y9(new Object[0]) * TICK_TO_MILLIS
            );
        }
        if (core.sZ(new Object[0]).Bw(new Object[0]).Ys(new Object[0])) {
            new MetricsLite(core.getPlugin());
        }
        core.getLogger().log(Level.INFO, "LPX bootstrap completed.");
        CheckDispatcher dispatcher = new CheckDispatcher(core);
        PacketEvents.getAPI().getEventManager().registerListener(new PreViaPacketListener(dispatcher));
        PacketEvents.getAPI().getEventManager().registerListener(new PacketHandler(core, dispatcher, 0L));
        Bukkit.getServicesManager().register(LPX.class, core, (Plugin) core.getPlugin(), ServicePriority.Normal);
        for (org.bukkit.entity.Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            com.github.retrooper.packetevents.protocol.player.User user = PacketEvents.getAPI().getPlayerManager().getUser(onlinePlayer);
            if (user != null && user.getChannel() != null) {
                core.getRegistry().injectUser(user, user.getChannel());
            }
        }
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
