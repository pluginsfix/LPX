package pluginsfix.lpx.check.impl.payload;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.IntervalCounter;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.manager.CheckManagerImpl;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import pluginsfix.lpx.util.Timer;
public class PayloadCheckFlood extends AbstractCheckT<WrapperPlayClientPluginMessage> {
    private long lastPayloadAt;
    private int minDelayMs;
    private IntervalCounter burstCounter;
    public PayloadCheckFlood(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        minDelayMs = j_(new Object[0]).getInt(kD(new Object[]{"delay", 0L}), 1000);
        int max = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 15);
        burstCounter = new IntervalCounter(max);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return E2(args);
    }
    public CheckResult E2(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientPluginMessage packet = (WrapperPlayClientPluginMessage) args[2];
        String channel = packet.getChannelName();
        if (channel == null) {
            return CheckResult.ALLOW;
        }
        if (channel.equalsIgnoreCase("REGISTER") || channel.equalsIgnoreCase("minecraft:register")) {
            return CheckResult.ALLOW;
        }
        if (!CheckManagerImpl.L.contains(channel)) {
            return CheckResult.ALLOW;
        }
        if (burstCounter != null && burstCounter.increment()) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("rate", burstCounter.getMaxCounter())}
            });
            return CheckResult.DENY;
        }
        long elapsed = Timer.vS(new Object[]{lastPayloadAt});
        lastPayloadAt = Timer.Wz(new Object[0]);
        if (elapsed < minDelayMs) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("delay", elapsed)}
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
