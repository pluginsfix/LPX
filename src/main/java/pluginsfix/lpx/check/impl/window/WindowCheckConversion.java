package pluginsfix.lpx.check.impl.window;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.window.ClickWindowConversionCheck;
public class WindowCheckConversion extends ClickWindowConversionCheck {
    public WindowCheckConversion(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult T7(Object[] args) {
        return checkConversion(new Object[]{
            (Long) args[2],
            (ProtocolPlayer) args[0],
            args[1]
        });
    }
}
