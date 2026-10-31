package pluginsfix.lpx.check.impl.sign;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.place.SignTextLengthCheck;
public class SignCheckTextLength extends SignTextLengthCheck {
    public SignCheckTextLength(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    protected CheckResult Ek(Object[] args) {
        return checkSignLength(new Object[]{
            (Long) args[2],
            (ProtocolPlayer) args[0],
            args[1]
        });
    }
}
