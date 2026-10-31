package pluginsfix.lpx.check.impl.place;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import pluginsfix.lpx.util.Timer;
import org.bukkit.entity.Player;
import org.bukkit.Location;
public class PlaceCheckReach extends AbstractCheckT<WrapperPlayClientPlayerBlockPlacement> {
    private static final long TELEPORT_GRACE_MS = 500L;
    private double a;
    public PlaceCheckReach(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        a = j_(new Object[0]).getDouble(kD(new Object[]{"max", 0L}), 50.0D);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        WrapperPlayClientPlayerBlockPlacement packet = (WrapperPlayClientPlayerBlockPlacement) args[2];
        return iv(new Object[]{token, player, packet});
    }
    protected CheckResult iv(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPlayerBlockPlacement packet = (WrapperPlayClientPlayerBlockPlacement) args[2];
        Player player = protocolPlayer.getPlayer().orElse(null);
        if (player == null) {
            return CheckResult.ALLOW;
        }
        if (Timer.vS(new Object[]{protocolPlayer.getLastTeleport()}) < TELEPORT_GRACE_MS) {
            jv(new Object[0]);
            return CheckResult.ALLOW;
        }
        Vector3i block = packet.getBlockPosition();
        double x = block.getX();
        double z = block.getZ();
        if (x == -1.0D || z == -1.0D) {
            jv(new Object[0]);
            return CheckResult.ALLOW;
        }
        Location location = player.getLocation();
        double horizontalDistance = Math.abs(x - location.getX()) + Math.abs(z - location.getZ());
        if (horizontalDistance <= a) {
            jv(new Object[0]);
            return CheckResult.ALLOW;
        }
        AH(new Object[]{
            0,
            token,
            packet,
            new FlagInfo[]{
                FlagInfo.of("x", x),
                FlagInfo.of("z", z),
                FlagInfo.of("player-x", location.getX()),
                FlagInfo.of("player-z", location.getZ()),
                FlagInfo.of("distance", (int) horizontalDistance)
            }
        });
        return CheckResult.DENY;
    }
}
