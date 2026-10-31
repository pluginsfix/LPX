package pluginsfix.lpx.check.impl.payload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.bukkit.entity.Player;
public class PayloadCheckCoordinates extends AbstractCheckT<WrapperPlayClientPluginMessage> {
    public PayloadCheckCoordinates(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return ug(args);
    }
    public CheckResult ug(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPluginMessage packet = (WrapperPlayClientPluginMessage) args[2];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        String channel = packet.getChannelName();
        if (!isCoordinatePayloadChannel(channel)) {
            return CheckResult.ALLOW;
        }
        ByteBuf buffer = null;
        try {
            byte[] data = packet.getData();
            if (data == null) {
                AZ(new Object[]{
                    packet,
                    token,
                    new FlagInfo[]{FlagInfo.of("exception", "null_payload")}
                });
                return CheckResult.DENY;
            }
            buffer = Unpooled.copiedBuffer(data);
            Vector3i position = new Vector3i(ByteBufHelper.readLong(buffer));
            boolean loaded = bukkitPlayer.getWorld().isChunkLoaded(position.getX() >> 4, position.getZ() >> 4);
            if (!loaded) {
                AZ(new Object[]{
                    packet,
                    token,
                    new FlagInfo[]{FlagInfo.of("position", position)}
                });
                return CheckResult.DENY;
            }
            return CheckResult.ALLOW;
        } catch (Exception exception) {
            AZ(new Object[]{
                packet,
                token,
                new FlagInfo[]{FlagInfo.of("exception", exception.getMessage())}
            });
            return CheckResult.DENY;
        } finally {
            if (buffer != null && buffer.refCnt() > 0) {
                buffer.release();
            }
        }
    }
    private boolean isCoordinatePayloadChannel(String channel) {
        if (channel == null) {
            return false;
        }
        return channel.equalsIgnoreCase("MC|AdvCdm")
            || channel.equalsIgnoreCase("MC|AutoCmd")
            || channel.equalsIgnoreCase("minecraft:adv_cmd")
            || channel.equalsIgnoreCase("minecraft:auto_cmd");
    }
}
