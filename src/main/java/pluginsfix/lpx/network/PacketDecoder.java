package pluginsfix.lpx.network;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.MessageToMessageDecoder;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.config.PunishConfig;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
@ChannelHandler.Sharable
public class PacketDecoder extends MessageToMessageDecoder<Object> {
    private static final long MIN_PACKET_COUNT_FOR_HEAVY_LOG = 5L;
    private final LPXCore core;
    private final ProtocolPlayer player;
    private static PacketWrapper[] legacyPacketWrapperCache;
    public PacketDecoder(long ignoredA, int ignoredB, LPXCore core, ProtocolPlayer player) {
        this.core = core;
        this.player = player;
    }
    @Override
    protected void decode(ChannelHandlerContext context, Object packet, List<Object> out) {
        ReceivedPacket receivedPacket = player.getLastReceivedPacket();
        if (receivedPacket != null) {
            player.setLastReceivedPacket(null);
            if (receivedPacket.getPacket() == null) {
                receivedPacket.setPacket(packet);
            }
            EventLoop eventLoop = context.channel().eventLoop();
            long ageInTicks = (long) core.sq(new Object[0]).ny(new Object[0]) - receivedPacket.getReceivedAtTick();
            core.sq(new Object[0]).h_(new Object[]{ageInTicks, 0L, eventLoop});
            logHeavyPacketIfNeeded(receivedPacket);
        }
        out.add(packet);
    }
    private void logHeavyPacketIfNeeded(ReceivedPacket receivedPacket) {
        PunishConfig packetLoggerConfig = core.sZ(new Object[0]).BJ(new Object[0]);
        if (!packetLoggerConfig.Xu(new Object[0])) {
            return;
        }
        String packetName = receivedPacket.getPacketType().getName();
        boolean matchesHeavyThreshold = packetLoggerConfig.Zv(new Object[]{0, 0, 0, receivedPacket.getElapsedTime()});
        if (!matchesHeavyThreshold) {
            return;
        }
        if (packetLoggerConfig.Jt(new Object[]{packetName})) {
            return;
        }
        if (!packetLoggerConfig.Jq(new Object[]{0L, receivedPacket.getPlayerName()})) {
            return;
        }
        if (player.getPacketCount() <= MIN_PACKET_COUNT_FOR_HEAVY_LOG) {
            return;
        }
        if (!packetLoggerConfig.Jn(new Object[]{0L, packetName})) {
            return;
        }
        if (!core.canPacketLogHeavy()) {
            return;
        }
        if (player.isKicked()) {
            return;
        }
        core.sY(new Object[0]).X3(new Object[]{receivedPacket, 0L});
    }
    public static void setLegacyPacketWrapperCache(PacketWrapper[] packetWrapperArray) {
        legacyPacketWrapperCache = packetWrapperArray;
    }
    public static PacketWrapper[] getLegacyPacketWrapperCache() {
        return legacyPacketWrapperCache;
    }
    public static void NT(PacketWrapper[] packetWrapperArray) {
        setLegacyPacketWrapperCache(packetWrapperArray);
    }
    public static PacketWrapper[] Lq() {
        return getLegacyPacketWrapperCache();
    }
}
