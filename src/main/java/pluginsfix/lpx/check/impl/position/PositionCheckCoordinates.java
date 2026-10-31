package pluginsfix.lpx.check.impl.position;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import pluginsfix.lpx.util.Timer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
public class PositionCheckCoordinates extends AbstractCheckT<WrapperPlayClientPlayerFlying> {
    private int N;
    private boolean L;
    private boolean p;
    private int l;
    private int m;
    private String u = "";
    public PositionCheckCoordinates(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        N = j_(new Object[0]).getInt(kD(new Object[]{"teleport-grace-ms", 0L}), 2000);
        L = j_(new Object[0]).getBoolean(kD(new Object[]{"setback", 0L}), false);
        p = j_(new Object[0]).getBoolean(kD(new Object[]{"ignore-flying-state", 0L}), false);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPlayerFlying packet = (WrapperPlayClientPlayerFlying) args[2];
        return Cw(new Object[]{token, protocolPlayer, packet});
    }
    public CheckResult Cw(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPlayerFlying packet = (WrapperPlayClientPlayerFlying) args[2];
        Player player = protocolPlayer.getPlayer().orElse(null);
        if (player == null) {
            return CheckResult.ALLOW;
        }
        if (!packet.hasPositionChanged()) {
            return CheckResult.ALLOW;
        }
        if (p && WrapperPlayClientPlayerFlying.isFlying(packet.getPacketTypeData().getPacketType())) {
            return CheckResult.ALLOW;
        }
        if (Timer.vS(new Object[]{protocolPlayer.getLastTeleport()}) < N) {
            sq(new Object[]{player, protocolPlayer, packet, token});
            return CheckResult.ALLOW;
        }
        Location location = player.getLocation();
        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;
        World world = player.getWorld();
        String worldName = world.getName();
        if (world.isChunkLoaded(chunkX, chunkZ)) {
            sq(new Object[]{player, protocolPlayer, packet, token});
            return CheckResult.ALLOW;
        }
        if (chunkX == l && chunkZ == m && worldName.equals(u)) {
            return CheckResult.DENY;
        }
        AZ(new Object[]{
            packet,
            token,
            new FlagInfo[]{
                FlagInfo.of("world", worldName),
                FlagInfo.of("chunk-x", chunkX),
                FlagInfo.of("chunk-z", chunkZ)
            }
        });
        if (L) {
            FoliaScheduler.getEntityScheduler().run(
                (Entity) player,
                getOptions().getCheckOptions().getPlugin(),
                task -> {
                    player.teleport(world.getSpawnLocation());
                    protocolPlayer.updateLastTeleport();
                },
                () -> {}
            );
        }
        sq(new Object[]{player, protocolPlayer, packet, token});
        return CheckResult.DENY;
    }
    private void sq(Object[] args) {
        Player player = (Player) args[0];
        this.l = player.getLocation().getBlockX() >> 4;
        this.m = player.getLocation().getBlockZ() >> 4;
        this.u = player.getWorld().getName();
    }
}
