package pluginsfix.lpx.check.place;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import org.bukkit.entity.Player;
import org.bukkit.Location;
public class DigReachCheck extends AbstractCheckT<WrapperPlayClientPlayerDigging> {
    private double maxDistanceSquared;
    public DigReachCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxDistanceSquared = j_(new Object[0]).getDouble(kD(new Object[]{"max", 0L}), 50.0D);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkDigDistance(args);
    }
    public CheckResult checkDigDistance(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPlayerDigging packet = (WrapperPlayClientPlayerDigging) args[2];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        if (packet.getAction().ordinal() > 2) {
            return CheckResult.ALLOW;
        }
        Location playerLocation = bukkitPlayer.getLocation();
        Vector3i blockPosition = packet.getBlockPosition();
        double dx = playerLocation.getX() - (blockPosition.getX() + 0.5D);
        double dy = playerLocation.getY() - (blockPosition.getY() + 0.5D) + 1.5D;
        double dz = playerLocation.getZ() - (blockPosition.getZ() + 0.5D);
        double distanceSquared = dx * dx + dy * dy + dz * dz;
        if (distanceSquared > maxDistanceSquared) {
            AH(new Object[]{
                0,
                token,
                packet,
                new FlagInfo[]{
                    FlagInfo.of("x", dx),
                    FlagInfo.of("y", dy),
                    FlagInfo.of("z", dz),
                    FlagInfo.of("distance", (int) distanceSquared)
                }
            });
            return CheckResult.DENY;
        }
        jv(new Object[0]);
        return CheckResult.ALLOW;
    }
}
