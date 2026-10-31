package pluginsfix.lpx.check.impl.payload;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
public class PayloadCheckChannel extends AbstractCheckT<WrapperPlayClientPluginMessage> {
    private static final Pattern NAMESPACED_CHANNEL = Pattern.compile("^[a-z0-9/._-]+:[a-z0-9/._-]+$");
    private static final Pattern LEGACY_CHANNEL = Pattern.compile("^[A-Za-z0-9_\\-]+\\|[A-Za-z0-9_\\-]+$");
    public PayloadCheckChannel(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return ia(args);
    }
    public CheckResult ia(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPluginMessage packet = (WrapperPlayClientPluginMessage) args[2];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        String channel = packet.getChannelName();
        String error = validate(channel, packet.getData());
        if (error == null) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            packet,
            token,
            new FlagInfo[]{
                FlagInfo.of("channel", String.valueOf(channel)),
                FlagInfo.of("reason", error)
            }
        });
        return CheckResult.DENY;
    }
    private String validate(String channel, byte[] payload) {
        if (channel == null || channel.isEmpty()) {
            return "empty_channel";
        }
        if (channel.length() > 128) {
            return "channel_too_long";
        }
        if (payload == null) {
            return "null_payload";
        }
        if (hasControlChars(channel)) {
            return "control_characters";
        }
        if (channel.startsWith("MC|")) {
            return null;
        }
        if (NAMESPACED_CHANNEL.matcher(channel).matches()) {
            return null;
        }
        if (LEGACY_CHANNEL.matcher(channel).matches()) {
            return null;
        }
        return "invalid_format";
    }
    private boolean hasControlChars(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isISOControl(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
