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
        if (Math.abs(yaw) > maxYawAbs) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("yaw", yaw)}
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
}
