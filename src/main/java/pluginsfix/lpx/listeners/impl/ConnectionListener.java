package pluginsfix.lpx.listeners.impl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.PlayerRegistry;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import pluginsfix.lpx.network.PacketDecoder;
import pluginsfix.lpx.player.ProtocolPlayerImpl;
import java.util.UUID;
public class ConnectionListener extends PacketListenerAbstract {
    private static final String DECODER_HANDLER = "decoder";
    private static final String LPX_DECODER_HANDLER = "lpx-decoder";
    private final LPXCore H;
    private final PlayerRegistry z;
    public ConnectionListener(LPXCore core, PlayerRegistry registry) {
        this.H = core;
        this.z = registry;
    }
    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        z.uninject(event.getUser());
    }
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Login.Server.LOGIN_SUCCESS) {
            return;
        }
        event.getTasksAfterSend().add(() -> KN(new Object[]{0L, event.getUser(), event.getChannel()}));
    }
    private void KN(Object[] args) {
        User user = (User) args[1];
        Channel channel = (Channel) args[2];
        UUID uuid = user.getUUID();
        H.oK(new Object[]{0L, (java.util.function.Supplier<String>) () ->
            "Setup connection for " + user.getName() + " (" + uuid + ")"
        });
        ProtocolPlayerImpl protocolPlayer = new ProtocolPlayerImpl(H, user, 0L);
        ProtocolPlayer replaced = z.addPlayer(user, protocolPlayer);
        if (replaced != null) {
            H.getLogger().warning("Replaced existing player entry during inject: " + user.getName());
        }
        if (H.sZ(new Object[0]).Bw(new Object[0]).YJ(new Object[0]) && uuid.getMostSignificantBits() == 0L) {
            protocolPlayer.setBedrock(true);
        }
        mp(new Object[]{channel, user, protocolPlayer, 0L});
    }
    public void mp(Object[] args) {
        Channel channel = (Channel) args[0];
        User user = (User) args[1];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[2];
        H.oK(new Object[]{0L, (java.util.function.Supplier<String>) () ->
            "Inject decoder in thread " + Thread.currentThread().getName() + " for " + user.getName() + " " + user.getUUID()
        });
        ChannelPipeline pipeline = channel.pipeline();
        if (pipeline.get(LPX_DECODER_HANDLER) != null) {
            pipeline.remove(LPX_DECODER_HANDLER);
        }
        PacketDecoder packetDecoder = new PacketDecoder(0L, 0, H, protocolPlayer);
        if (pipeline.get(DECODER_HANDLER) != null) {
            pipeline.addAfter(DECODER_HANDLER, LPX_DECODER_HANDLER, packetDecoder);
        } else {
            pipeline.addLast(LPX_DECODER_HANDLER, packetDecoder);
        }
        H.oK(new Object[]{0L, (java.util.function.Supplier<String>) () ->
            "Injected " + user.getName() + ", pipeline=" + pipeline.toMap()
        });
        H.sq(new Object[0]).kN(new Object[]{protocolPlayer.getUniqueId(), Thread.currentThread(), channel.eventLoop()});
    }
}
