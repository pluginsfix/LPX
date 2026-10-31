package pluginsfix.lpx.player;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.PlayerRegistry;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.listeners.impl.ConnectionListener;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
public class PlayerRegistryImpl implements PlayerRegistry {
    private final LPXCore core;
    private final Map<User, ProtocolPlayer> playersByUser;
    private static String[] legacyState;
    public PlayerRegistryImpl(int ignoredA, LPXCore core, int ignoredB, char ignoredC) {
        this.playersByUser = new ConcurrentHashMap<>();
        this.core = core;
        PacketEvents.getAPI().getEventManager().registerListener(new ConnectionListener(core, this));
    }
    @Override
    public void uninjectAll() {
        for (User user : new HashSet<>(playersByUser.keySet())) {
            uninject(user);
        }
    }
    @Override
    public void uninject(User user) {
        ProtocolPlayer removed = playersByUser.remove(user);
        if (removed == null) {
            return;
        }
        UUID playerId = user.getUUID();
        if (playerId != null) {
            core.s1(new Object[0]).removePlayer(playerId);
        }
        Object rawChannel = user.getChannel();
        Channel channel = rawChannel instanceof Channel ? (Channel) rawChannel : null;
        if (channel == null) {
            return;
        }
        EventLoop eventLoop = channel.eventLoop();
        if (eventLoop == null || playerId == null) {
            return;
        }
        eventLoop.execute(() ->
            core.sq(new Object[0]).kO(new Object[]{playerId, Thread.currentThread(), eventLoop})
        );
    }
    @Override
    public Optional<ProtocolPlayer> getPlayer(Player player) {
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        if (user == null) {
            return Optional.empty();
        }
        return getPlayer(user);
    }
    @Override
    public Optional<ProtocolPlayer> getPlayer(User user) {
        return Optional.ofNullable(playersByUser.get(user));
    }
    @Override
    public Map<User, ProtocolPlayer> getPlayers() {
        return Collections.unmodifiableMap(playersByUser);
    }
    @Override
    @Nullable
    public ProtocolPlayer addPlayer(User user, ProtocolPlayer player) {
        return playersByUser.put(user, player);
    }
    public static void qn(String[] state) {
        legacyState = state;
    }
    public static String[] yd() {
        return legacyState;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
