package pluginsfix.lpx.check.impl.tab;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class TabCheckFlood extends AbstractCheckT<WrapperPlayClientTabComplete> {
    private static final List<String> DEFAULT_BLOCKED_PARTS =
        Arrays.asList("[", "]", "{", "}", "@", "=", "nbt");
    private int maxLength;
    private List<String> blockedParts;
    private static String[] legacyState;
    public TabCheckFlood(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxLength = j_(new Object[0]).getInt(kD(new Object[]{"max-length", 0L}), 256);
        blockedParts = j_(new Object[0]).getStringList(kD(new Object[]{"characters", 0L}));
        if (blockedParts == null || blockedParts.isEmpty()) {
            blockedParts = new ArrayList<>(DEFAULT_BLOCKED_PARTS);
        }
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return rC(args);
    }
    protected CheckResult rC(Object[] args) {
        long token = (Long) args[0];
        WrapperPlayClientTabComplete packet = (WrapperPlayClientTabComplete) args[2];
        String text = packet.getText();
        if (text == null) {
            return CheckResult.ALLOW;
        }
        if (text.length() > maxLength) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{
                    FlagInfo.of("length", text.length()),
                    FlagInfo.of("max", maxLength)
                }
            });
            return CheckResult.DENY;
        }
        for (String blockedPart : blockedParts) {
            if (blockedPart != null && !blockedPart.isEmpty() && text.contains(blockedPart)) {
                AZ(new Object[]{
                    packet,
                    token,
                    new FlagInfo[]{
                        FlagInfo.of("text", text),
                        FlagInfo.of("contains", blockedPart)
                    }
                });
                return CheckResult.DENY;
            }
        }
        return CheckResult.ALLOW;
    }
    public static void u5(String[] value) {
        legacyState = value;
    }
    public static String[] mc() {
        return legacyState;
    }
}
