package pluginsfix.lpx.core;
import io.netty.channel.Channel;
import pluginsfix.lpx.command.CommandRouter;
import pluginsfix.lpx.alert.AlertManager;
import pluginsfix.lpx.alert.QueuedFlag;
import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import pluginsfix.lpx.api.check.abstraction.CheckManager;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.PlayerRegistry;
import pluginsfix.lpx.check.manager.CheckManagerImpl;
import pluginsfix.lpx.config.ConfigManager;
import pluginsfix.lpx.gui.GuiManager;
import pluginsfix.lpx.gui.GuiManagerImpl;
import pluginsfix.lpx.gui.InventoryViewAdapterLegacy;
import pluginsfix.lpx.gui.InventoryViewAdapterModern;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import pluginsfix.lpx.network.NetworkStatsCollector;
import pluginsfix.lpx.network.PacketLogger;
import pluginsfix.lpx.network.WatchdogMonitor;
import pluginsfix.lpx.player.PlayerRegistryImpl;
import pluginsfix.lpx.util.ChannelUtils;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;
import java.util.logging.Level;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
public class LPXCore implements LPX {
    private static final long HEAVY_PACKET_LOG_GRACE_MS = 30_000L;
    private final pluginsfix.lpx.api.LPXPlugin plugin;
    private final PlayerRegistry registry;
    private final CheckManager checkManager;
    private final ConfigManager configManager;
    private final GuiManagerImpl guiManagerImpl;
    private final GuiManager guiManager;
    private final PacketLogger packetLogger;
    private final AlertManager alertManager;
    private final ArrayDeque<QueuedFlag> queuedFlags;
    private final NetworkStatsCollector networkStatsCollector;
    private final WatchdogMonitor watchdogMonitor;
    private boolean updated;
    private boolean disabled;
    private long firstJoinTime;
    private static String[] legacyState;
    public LPXCore(pluginsfix.lpx.api.LPXPlugin plugin, long ignored) {
        this.updated = true;
        this.disabled = false;
        this.plugin = plugin;
        this.queuedFlags = new ArrayDeque<>();
        this.configManager = new ConfigManager(0L, plugin);
        this.alertManager = new AlertManager(0L, this);
        this.registry = new PlayerRegistryImpl(0, this, 0, (char) 0);
        this.guiManagerImpl = new GuiManagerImpl(this);
        this.guiManager = new GuiManager(this, 0L);
        this.packetLogger = new PacketLogger(this, (char) 0, 0, (char) 0);
        this.checkManager = new CheckManagerImpl(this);
        this.networkStatsCollector = new NetworkStatsCollector(this, 0L);
        this.watchdogMonitor = new WatchdogMonitor(0L, this);
        PacketEvents.getAPI().init();
        try {
            new PluginBootstrap().q(this);
        } catch (Exception exception) {
            getLogger().log(Level.SEVERE, "Unable to bootstrap LPX.", exception);
            disable(false);
        }
    }
    @Override
    public void disable(boolean fromPluginLifecycle) {
        if (disabled) {
            return;
        }
        disabled = true;
        try {
            if (guiManagerImpl != null) {
                for (Player p : guiManagerImpl.getOpenedInventoriesSnapshot().keySet()) {
                    p.closeInventory();
                }
            }
        } catch (Throwable ignored) {
        }
        if (!fromPluginLifecycle) {
            plugin.getServer().getPluginManager().disablePlugin((Plugin) plugin);
        }
        try {
            registry.uninjectAll();
        } catch (Throwable ignored) {
        }
        try {
            packetLogger.NU(new Object[]{0L});
        } catch (Throwable ignored) {
        }
        try {
            watchdogMonitor.to(new Object[]{0, 0, 0});
        } catch (Throwable ignored) {
        }
        try {
            networkStatsCollector.nz(new Object[]{0L});
        } catch (Throwable ignored) {
        }
        try {
            PacketEvents.getAPI().terminate();
        } catch (Throwable ignored) {
        }
        alertManager.flushLogger();
    }
    @Override
    public void reloadConfig() {
        configManager.cF(new Object[]{this, 0L});
    }
    @Override
    public InventoryViewAdapter getInventoryViewAdapter(Player player) {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            return new InventoryViewAdapterModern(player, 0L);
        }
        return new InventoryViewAdapterLegacy(player, 0L);
    }
    public void oq(Object[] args) {
        Listener listener = (Listener) args[0];
        plugin.getServer().getPluginManager().registerEvents(listener, (Plugin) plugin);
    }
    public void wb(Object... args) {
        Listener[] listeners = (Listener[]) args[1];
        for (Listener listener : listeners) {
            oq(new Object[]{listener});
        }
    }
    @Override
    public String getServer() {
        return configManager.Bw(new Object[0]).YQ(new Object[0]);
    }
    @Override
    public pluginsfix.lpx.api.LPXPlugin getPlugin() {
        return plugin;
    }
    @Override
    public PlayerRegistry getRegistry() {
        return registry;
    }
    @Override
    public CheckManager getCheckManager() {
        return checkManager;
    }
    public ConfigManager sZ(Object[] ignored) {
        return configManager;
    }
    public AlertManager s1(Object[] ignored) {
        return alertManager;
    }
    public PacketLogger sY(Object[] ignored) {
        return packetLogger;
    }
    @Override
    public boolean isUpdated() {
        return updated;
    }
    @Override
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
    public GuiManagerImpl sJ(Object[] ignored) {
        return guiManagerImpl;
    }
    public GuiManager sx(Object[] ignored) {
        return guiManager;
    }
    public Deque<QueuedFlag> sN(Object[] ignored) {
        return queuedFlags;
    }
    public NetworkStatsCollector sq(Object[] ignored) {
        return networkStatsCollector;
    }
    public WatchdogMonitor s9(Object[] ignored) {
        return watchdogMonitor;
    }
    @Override
    public long getFirstJoinTime() {
        return firstJoinTime;
    }
    @Override
    public void setFirstJoinTime(long firstJoinTime) {
        this.firstJoinTime = firstJoinTime;
    }
    @Override
    public boolean canPacketLogHeavy() {
        return firstJoinTime > 0L && System.currentTimeMillis() - firstJoinTime > HEAVY_PACKET_LOG_GRACE_MS;
    }
    public void Cs(Object[] args) {
        Player player = (Player) args[0];
        String reason = (String) args[1];
        registry.getPlayer(player).ifPresent(protocolPlayer -> protocolPlayer.kick(reason));
    }
    public void Fs(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[0];
        String message = (String) args[2];
        User user = protocolPlayer.getUser();
        protocolPlayer.getPlayer().ifPresent(player -> PacketEvents.getAPI()
            .getPlayerManager()
            .sendPacket(player, new WrapperPlayServerDisconnect(LegacyComponentSerializer.legacyAmpersand().deserialize(message))));
        try {
            if (user != null) {
                Object rawChannel = user.getChannel();
                Channel channel = rawChannel instanceof Channel ? (Channel) rawChannel : null;
                if (channel != null) {
                    ChannelUtils.Mv(new Object[]{channel});
                }
            }
        } catch (Exception exception) {
            String username = user == null ? "unknown" : user.getName();
            getLogger().log(Level.SEVERE, "Unable to close channel for " + username, exception);
        }
    }
    public void oK(Object[] args) {
        @SuppressWarnings("unchecked")
        Supplier<String> supplier = (Supplier<String>) args[1];
        if (configManager.Bw(new Object[0]).Yk(new Object[0])) {
            getLogger().info(supplier.get());
        }
    }
    public static void wv(String[] state) {
        legacyState = state;
    }
    public static String[] sp() {
        return legacyState;
    }
    private static Exception a(Exception exception) {
        return exception;
    }
}
