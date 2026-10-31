package pluginsfix.lpx.check.impl.netty;
import io.netty.buffer.ByteBuf;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.configuration.file.FileConfiguration;
public class NettyCheckPacketSize extends AbstractCheckT<ByteBuf> {
    private int U;
    private final boolean H;
    private static int[] I;
    private static final double j = 1.5D;
    public NettyCheckPacketSize(ProtocolPlayer player, CheckOptions options, boolean preVia) {
        super(player, options);
        this.H = preVia;
    }
    @Override
    public void loadOptions() {
        FileConfiguration config = j_(new Object[0]);
        boolean auto = config.getBoolean(kD(new Object[]{"auto-limit", 0L}),
            config.getBoolean(kD(new Object[]{"auto", 0L}), true));
        if (auto) {
            U = Xf(new Object[]{0L});
            return;
        }
        U = config.getInt(kD(new Object[]{"max-size", 0L}),
            config.getInt(kD(new Object[]{"limit", 0L}), Xf(new Object[]{0L})));
    }
    private int Xf(Object[] args) {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        if (version.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
            return 8192;
        }
        if (version.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
            return 16384;
        }
        return 32768;
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        ByteBuf buffer = (ByteBuf) args[2];
        long token = (Long) args[0];
        return pj(new Object[]{player, buffer, token});
    }
    public CheckResult pj(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        ByteBuf buffer = (ByteBuf) args[1];
        long token = (Long) args[2];
        long packetSize = player.getLastReceivedPacket().getSize();
        double limit = U;
        if (!H) {
            limit *= j;
        }
        if (packetSize <= limit) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            buffer,
            token,
            new FlagInfo[]{
                FlagInfo.of("size", packetSize),
                FlagInfo.of("limit", U),
                FlagInfo.of("pre-via", H)
            }
        });
        return CheckResult.DENY;
    }
    @Override
    public boolean isPreVia() {
        return H;
    }
    public static void UV(int[] nArray) {
        I = nArray;
    }
    public static int[] XR() {
        return I;
    }
}
