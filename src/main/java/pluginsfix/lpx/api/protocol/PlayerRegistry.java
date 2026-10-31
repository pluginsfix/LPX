package pluginsfix.lpx.api.protocol;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
public interface PlayerRegistry {
    public Optional<ProtocolPlayer> getPlayer(User var1);
    public Map<User, ProtocolPlayer> getPlayers();
    public void uninjectAll();
    public void uninject(User var1);
    public Optional<ProtocolPlayer> getPlayer(Player var1);
    @Nullable
    public ProtocolPlayer addPlayer(User var1, ProtocolPlayer var2);
    void injectUser(User user, Object channel);
}
