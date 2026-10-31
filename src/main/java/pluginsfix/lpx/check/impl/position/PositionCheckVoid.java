package pluginsfix.lpx.check.impl.position;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.position.VoidYCheck;
public class PositionCheckVoid extends VoidYCheck {
    public PositionCheckVoid(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult im(Object[] args) {
        return checkVoid(args);
    }
}
