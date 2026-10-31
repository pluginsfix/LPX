package pluginsfix.lpx.check.impl.window;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.window.PickEntityIncludeDataCheck;
public class WindowCheckPickEntity extends PickEntityIncludeDataCheck {
    public WindowCheckPickEntity(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult Wu(Object[] args) {
        return checkPickFromEntity(new Object[]{
            (Long) args[1],
            (ProtocolPlayer) args[0],
            args[2]
        });
    }
}
