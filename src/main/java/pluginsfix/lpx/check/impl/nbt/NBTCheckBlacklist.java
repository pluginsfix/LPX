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
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import pluginsfix.lpx.player.ProtocolPlayerImpl;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class NBTCheckBlacklist extends AbstractCheckT<NBTDecodeEvent> {
    private boolean d;
    private Map<String, Integer> B = new HashMap<>();
    public NBTCheckBlacklist(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        d = j_(new Object[0]).getBoolean(kD(new Object[]{"strict", 0L}), false);
        B = new HashMap<>();
        List<String> rawEntries = j_(new Object[0]).getStringList(kD(new Object[]{"blacklist", 0L}));
        for (String rawEntry : rawEntries) {
            String[] split = rawEntry.split(",");
            if (split.length < 2) {
                logWarning(() -> "Invalid blacklist rule: " + rawEntry + " (expected path,limit)");
                continue;
            }
            String path = split[0].trim();
            try {
                int limit = Integer.parseInt(split[1].trim());
                B.put(path, limit);
            } catch (NumberFormatException numberFormatException) {
                logWarning(() -> "Invalid numeric blacklist rule: " + rawEntry);
            }
        }
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        return Sp(new Object[]{player, token, event});
    }
    public CheckResult Sp(Object[] args) {
        long token = (Long) args[1];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        if (B.isEmpty()) {
            return CheckResult.ALLOW;
        }
        NBT root = event.getNbt();
        if (!(root instanceof NBTCompound compound)) {
            return CheckResult.ALLOW;
        }
        for (Map.Entry<String, Integer> rule : B.entrySet()) {
            String path = rule.getKey();
            int limit = rule.getValue();
            NBT tag = findTagByPath(compound, path);
            if (tag == null) {
                continue;
            }
            int measured = measure(tag);
            boolean deny = limit <= 0 || measured > limit || (d && measured < 0);
            if (!deny) {
                continue;
            }
            AZ(new Object[]{
                event,
                token,
                new FlagInfo[]{
                    FlagInfo.of("path", path),
                    FlagInfo.of("value", measured),
                    FlagInfo.of("limit", limit)
                }
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private NBT findTagByPath(NBTCompound root, String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        if (!path.contains(".")) {
            return root.getTagOrNull(path);
        }
        String[] parts = path.split("\\.");
        NBT current = root;
        for (int i = 0; i < parts.length; i++) {
            if (!(current instanceof NBTCompound compound)) {
                return null;
            }
            current = compound.getTagOrNull(parts[i]);
            if (current == null) {
                return null;
            }
            if (i == parts.length - 1) {
                return current;
            }
        }
        return null;
    }
    private int measure(NBT tag) {
        if (tag instanceof NBTString nbtString) {
            return nbtString.getValue().length();
        }
        if (tag instanceof NBTList<?> list) {
            return list.size();
        }
        if (tag instanceof NBTCompound compound) {
            return compound.getTags().size();
        }
        return -1;
    }
    private void logWarning(Supplier<String> supplier) {
        if (F instanceof ProtocolPlayerImpl protocolPlayer) {
            protocolPlayer.mk(new Object[0]).getLogger().warning(supplier.get());
        }
    }
    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}
