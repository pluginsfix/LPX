package pluginsfix.lpx.check.place;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import java.nio.charset.StandardCharsets;
public class SignTextLengthCheck extends AbstractCheckT<WrapperPlayClientUpdateSign> {
    private double maxBytes;
    public SignTextLengthCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxBytes = j_(new Object[0]).getDouble(kD(new Object[]{"max", 0L}), 70.0D);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkSignLength(args);
    }
    public CheckResult checkSignLength(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientUpdateSign packet = (WrapperPlayClientUpdateSign) args[2];
        for (String line : packet.getTextLines()) {
            int lineBytes = line.getBytes(StandardCharsets.UTF_8).length;
            if (lineBytes > maxBytes) {
                AZ(new Object[]{
                    packet,
                    token,
                    new FlagInfo[]{
                        FlagInfo.of("line", line),
                        FlagInfo.of("bytes", lineBytes)
                    }
                });
                return CheckResult.DENY;
            }
        }
        return CheckResult.ALLOW;
    }
}
