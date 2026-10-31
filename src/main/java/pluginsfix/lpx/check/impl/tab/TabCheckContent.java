package pluginsfix.lpx.check.impl.tab;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import pluginsfix.lpx.util.TextUtils;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.List;
public class TabCheckContent extends AbstractCheckT<WrapperPlayClientTabComplete> {
    private List<String> starts = Collections.emptyList();
    private List<String> contains = Collections.emptyList();
    public TabCheckContent(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        starts = j_(new Object[0]).getStringList(kD(new Object[]{"starts", 0L}));
        contains = j_(new Object[0]).getStringList(kD(new Object[]{"contains", 0L}));
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return Yt(args);
    }
    protected CheckResult Yt(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientTabComplete packet = (WrapperPlayClientTabComplete) args[2];
        String text = packet.getText();
        if (text == null) {
            return CheckResult.ALLOW;
        }
        boolean deniedByStarts = matchesAny(starts, pattern -> TextUtils.yP(new Object[]{text, token, pattern}));
        boolean deniedByContains = matchesAny(contains, pattern -> TextUtils.yU(new Object[]{text, token, pattern}));
        if (deniedByStarts || deniedByContains) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("text", text)}
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    private boolean matchesAny(List<String> values, Predicate<String> predicate) {
        if (values == null || values.isEmpty()) {
            return false;
        }
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                continue;
            }
            if (predicate.test(value)) {
                return true;
            }
        }
        return false;
    }
}
