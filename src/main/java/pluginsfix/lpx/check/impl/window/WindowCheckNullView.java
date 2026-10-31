package pluginsfix.lpx.check.impl.window;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.window.ClickWindowNullViewCheck;
public class WindowCheckNullView extends ClickWindowNullViewCheck {
    public WindowCheckNullView(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult ZX(Object[] args) {
        return checkNullView(args);
    }
}
