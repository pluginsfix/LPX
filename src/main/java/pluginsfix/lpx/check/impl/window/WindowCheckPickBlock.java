package pluginsfix.lpx.check.impl.window;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.window.PickBlockIncludeDataCheck;
public class WindowCheckPickBlock extends PickBlockIncludeDataCheck {
    public WindowCheckPickBlock(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult Qi(Object[] args) {
        return checkPickFromBlock(new Object[]{
            (Long) args[2],
            (ProtocolPlayer) args[0],
            args[1]
        });
    }
}
