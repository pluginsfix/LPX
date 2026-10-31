package pluginsfix.lpx.check.impl.nbt;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.NBTDecodeEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
public class NBTCheckSize extends AbstractCheckT<NBTDecodeEvent> {
    private long t;
    private long g;
    public NBTCheckSize(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        t = readLong("max-count", "max", 2048L);
        g = readLong("max-depth", "depth", 64L);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        return Rt(new Object[]{player, token, event});
    }
    public CheckResult Rt(Object[] args) {
        long token = (Long) args[1];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        NBTParseResult result = Pp(new Object[]{"", event.getNbt(), token, 0, 0});
        if (result.gg(new Object[0])) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("count", result.gm(new Object[0])),
                FlagInfo.of("depth", result.gO(new Object[0])),
                FlagInfo.of("reason", String.valueOf(result.g9(new Object[0])))
            }
        });
        return CheckResult.DENY;
    }
    private NBTParseResult Pp(Object[] args) {
        NBT nbt = (NBT) args[1];
        int initialDepth = (Integer) args[3];
        int initialCount = (Integer) args[4];
        int[] count = new int[]{Math.max(0, initialCount)};
        int[] maxDepthSeen = new int[]{Math.max(0, initialDepth)};
        NBTLimitReason reason = scan(nbt, initialDepth + 1, count, maxDepthSeen);
        if (reason == NBTLimitReason.MAX_DEPTH_EXCEEDED) {
            return NBTParseResult.depthExceeded(maxDepthSeen[0], count[0]);
        }
        if (reason == NBTLimitReason.MAX_COUNT_EXCEEDED) {
            return NBTParseResult.countExceeded(maxDepthSeen[0], count[0]);
        }
        return NBTParseResult.success(maxDepthSeen[0], count[0]);
    }
    private NBTLimitReason scan(NBT node, int depth, int[] count, int[] maxDepthSeen) {
        if (node == null) {
            return null;
        }
        maxDepthSeen[0] = Math.max(maxDepthSeen[0], depth);
        if (depth > g) {
            return NBTLimitReason.MAX_DEPTH_EXCEEDED;
        }
        count[0]++;
        if (count[0] > t) {
            return NBTLimitReason.MAX_COUNT_EXCEEDED;
        }
        if (node instanceof NBTList<?> list) {
            for (NBT child : list.getTags()) {
                NBTLimitReason reason = scan(child, depth + 1, count, maxDepthSeen);
                if (reason != null) {
                    return reason;
                }
            }
            return null;
        }
        if (node instanceof NBTCompound compound) {
            for (NBT child : compound.getTags().values()) {
                NBTLimitReason reason = scan(child, depth + 1, count, maxDepthSeen);
                if (reason != null) {
                    return reason;
                }
            }
        }
        return null;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private long readLong(String primary, String legacy, long fallback) {
        String primaryPath = kD(new Object[]{primary, 0L});
        if (j_(new Object[0]).contains(primaryPath)) {
            return j_(new Object[0]).getLong(primaryPath, fallback);
        }
        return j_(new Object[0]).getLong(kD(new Object[]{legacy, 0L}), fallback);
    }
}
