package pluginsfix.lpx.check.impl.window;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.window.ClickWindowSlotBoundsCheck;
public class WindowCheckSlotBounds extends ClickWindowSlotBoundsCheck {
    public WindowCheckSlotBounds(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult gb(Object[] args) {
        return checkSlotBounds(args);
    }
}
