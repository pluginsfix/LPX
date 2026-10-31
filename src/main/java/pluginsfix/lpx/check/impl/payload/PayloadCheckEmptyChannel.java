package pluginsfix.lpx.check.impl.payload;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
public class PayloadCheckEmptyChannel extends AbstractCheckT<WrapperPlayClientPluginMessage> {
    public PayloadCheckEmptyChannel(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return gi(args);
    }
    public CheckResult gi(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientPluginMessage packet = (WrapperPlayClientPluginMessage) args[2];
        String channel = packet.getChannelName();
        if (channel != null && !channel.isEmpty()) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            packet,
            token,
            new FlagInfo[0]
        });
        return CheckResult.DENY;
    }
}
