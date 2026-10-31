package pluginsfix.lpx.check.impl.window;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.window.ClickWindowTypeCheck;
public class WindowCheckType extends ClickWindowTypeCheck {
    private static int[] legacyState;
    public WindowCheckType(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    public CheckResult jE(Object[] args) {
        return checkWindowType(new Object[]{
            (Long) args[2],
            (ProtocolPlayer) args[0],
            args[1]
        });
    }
    public static void Tg(int[] value) {
        legacyState = value;
    }
    public static int[] N7() {
        return legacyState;
    }
}
