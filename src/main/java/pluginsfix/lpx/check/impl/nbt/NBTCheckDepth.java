package pluginsfix.lpx.check.impl.nbt;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.NBTDecodeEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import java.util.Map;
public class NBTCheckDepth extends AbstractCheckT<NBTDecodeEvent> {
    private static final int X = 0;
    private int P;
    private int f;
    private boolean N;
    public NBTCheckDepth(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        P = j_(new Object[0]).getInt(kD(new Object[]{"max-depth", 0L}), 64);
        f = j_(new Object[0]).getInt(kD(new Object[]{"max-string-depth", 0L}), 32);
        boolean oldClient = F.getUser().getClientVersion().isOlderThan(ClientVersion.V_1_16);
        boolean modernServer = PacketEvents.getAPI()
            .getServerManager()
            .getVersion()
            .isNewerThanOrEquals(ServerVersion.V_1_16);
        N = oldClient && modernServer;
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        long token = (Long) args[0];
        return qv(new Object[]{player, event, token});
    }
    public CheckResult qv(Object[] args) {
        NBTDecodeEvent event = (NBTDecodeEvent) args[1];
        long token = (Long) args[2];
        if (F.usesComponents()) {
            return CheckResult.ALLOW;
        }
        return GG(new Object[]{"", event.getNbt(), token, event, 1});
    }
    private CheckResult GG(Object[] args) {
        String path = (String) args[0];
        NBT nbt = (NBT) args[1];
        long token = (Long) args[2];
        NBTDecodeEvent event = (NBTDecodeEvent) args[3];
        int depth = (Integer) args[4];
        if (nbt == null) {
            return CheckResult.ALLOW;
        }
        if (depth > P) {
            AZ(new Object[]{
                event,
                token,
                new FlagInfo[]{
                    FlagInfo.of("path", path),
                    FlagInfo.of("depth", depth),
                    FlagInfo.of("limit", P)
                }
            });
            return CheckResult.DENY;
        }
        if (nbt instanceof NBTString stringTag) {
            return _P(new Object[]{path, stringTag, token, event});
        }
        if (nbt instanceof NBTList<?> list) {
            for (int i = 0; i < list.size(); i++) {
                CheckResult result = GG(new Object[]{path + "[" + i + "]", list.getTag(i), token, event, depth + 1});
                if (result == CheckResult.DENY) {
                    return result;
                }
            }
            return CheckResult.ALLOW;
        }
        if (nbt instanceof NBTCompound compound) {
            for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
                String nextPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
                CheckResult result = GG(new Object[]{nextPath, entry.getValue(), token, event, depth + 1});
                if (result == CheckResult.DENY) {
                    return result;
                }
            }
        }
        return CheckResult.ALLOW;
    }
    private CheckResult _P(Object[] args) {
        String path = (String) args[0];
        NBTString stringTag = (NBTString) args[1];
        long token = (Long) args[2];
        NBTDecodeEvent event = (NBTDecodeEvent) args[3];
        String value = stringTag.getValue();
        if (value == null || value.isEmpty()) {
            return CheckResult.ALLOW;
        }
        if (!N) {
            return CheckResult.ALLOW;
        }
        int maxStringDepth = pr(new Object[]{value, token, P + 1});
        if (maxStringDepth == -1) {
            AZ(new Object[]{
                event,
                token,
                new FlagInfo[]{
                    FlagInfo.of("path", path),
                    FlagInfo.of("reason", "malformed-structure")
                }
            });
            return CheckResult.DENY;
        }
        if (maxStringDepth <= f) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("path", path),
                FlagInfo.of("string-depth", maxStringDepth),
                FlagInfo.of("limit", f)
            }
        });
        return CheckResult.DENY;
    }
    private int pr(Object[] args) {
        String value = (String) args[0];
        int hardLimit = (Integer) args[2];
        int depth = 0;
        int maxDepth = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (iU(new Object[]{0L, (int) c})) {
                depth++;
                maxDepth = Math.max(maxDepth, depth);
                if (maxDepth > hardLimit) {
                    return maxDepth;
                }
                continue;
            }
            if (c == '}' || c == ']' || c == ')') {
                depth--;
                if (depth < 0) {
                    return -1;
                }
            }
        }
        return depth == 0 ? maxDepth : -1;
    }
    private static boolean iU(Object[] args) {
        int codePoint = (Integer) args[1];
        return codePoint == '{'
            || codePoint == '['
            || codePoint == '(';
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
