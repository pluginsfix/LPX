package pluginsfix.lpx.check.position;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
public class RotationRangeCheck extends AbstractCheckT<WrapperPlayClientPlayerFlying> {
    private int maxYawAbs;
    public RotationRangeCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxYawAbs = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 300000);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkRotation(args);
    }
    public CheckResult checkRotation(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientPlayerFlying packet = (WrapperPlayClientPlayerFlying) args[2];
        if (!packet.hasRotationChanged()) {
            return CheckResult.ALLOW;
        }
        float yaw = packet.getLocation().getYaw();
        float pitch = packet.getLocation().getPitch();
        if (Float.isNaN(yaw) || Float.isNaN(pitch) || Float.isInfinite(yaw) || Float.isInfinite(pitch) || Math.abs(pitch) > 90.0F || Math.abs(yaw) > maxYawAbs) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("yaw", yaw), FlagInfo.of("pitch", pitch)}
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
}
