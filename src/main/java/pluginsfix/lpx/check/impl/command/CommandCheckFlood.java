package pluginsfix.lpx.check.impl.command;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.IntervalCounter;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
public class CommandCheckFlood extends AbstractCheckT<PlayerCommandPreprocessEvent> {
    private IntervalCounter counter;
    public CommandCheckFlood(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        int max = j_(new Object[0]).getInt(kD(new Object[]{"max-per-interval", 0L}), 20);
        int interval = j_(new Object[0]).getInt(kD(new Object[]{"interval-ms", 0L}), 1000);
        counter = new IntervalCounter(max, interval);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return SL(args);
    }
    protected CheckResult SL(Object[] args) {
        PlayerCommandPreprocessEvent event = (PlayerCommandPreprocessEvent) args[2];
        if (!counter.increment()) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            0L,
            new FlagInfo[]{FlagInfo.of("command", event.getMessage())}
        });
        return CheckResult.DENY;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
