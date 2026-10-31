package pluginsfix.lpx.check.impl.position;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.position.RotationRangeCheck;
public class PositionCheckRotation extends RotationRangeCheck {
    private static int[] legacyState;
    public PositionCheckRotation(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult jy(Object[] args) {
        return checkRotation(args);
    }
    public static void WH(int[] value) {
        legacyState = value;
    }
    public static int[] CL() {
        return legacyState;
    }
}
