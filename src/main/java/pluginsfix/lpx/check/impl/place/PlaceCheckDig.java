package pluginsfix.lpx.check.impl.place;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.place.DigReachCheck;
public class PlaceCheckDig extends DigReachCheck {
    private static int[] legacyState;
    public PlaceCheckDig(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    protected CheckResult Tq(Object[] args) {
        return checkDigDistance(new Object[]{
            (Long) args[1],
            (ProtocolPlayer) args[0],
            args[2]
        });
    }
    public static void mZ(int[] value) {
        legacyState = value;
    }
    public static int[] hj() {
        return legacyState;
    }
}
