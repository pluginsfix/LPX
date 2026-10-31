package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import java.nio.charset.StandardCharsets;
public class ItemCheckNBTList extends AbstractCheckT<ItemStackDecodeEvent> {
    private static final String PAGES_TAG = "pages";
    private int u;
    private int L;
    private boolean C;
    public ItemCheckNBTList(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        u = readInt("max-line-bytes", "max-line-length", 8192);
        L = readInt("max-pages", "max-list-size", 128);
        C = j_(new Object[0]).getBoolean(kD(new Object[]{"trim", 0L}),
            j_(new Object[0]).getBoolean(kD(new Object[]{"sanitize", 0L}), false));
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return MJ(new Object[]{player, token, event});
    }
    public CheckResult MJ(Object[] args) {
        long token = (Long) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        NBTCompound nbt = itemStack.getNBT();
        if (nbt == null) {
            return CheckResult.ALLOW;
        }
        NBTList<NBTString> pages = nbt.getStringListTagOrNull(PAGES_TAG);
        if (pages == null) {
            return CheckResult.ALLOW;
        }
        int pageCount = pages.size();
        int totalMaxBytes = Math.max(u, 1) * Math.max(L, 1);
        boolean changed = false;
        int totalBytes = 0;
        for (int i = 0; i < pages.size(); i++) {
            NBTString page = pages.getTag(i);
            String value = page.getValue();
            int bytes = value.getBytes(StandardCharsets.UTF_8).length;
            totalBytes += bytes;
            if (bytes <= u) {
                continue;
            }
            if (!C) {
                AZ(new Object[]{
                    event,
                    token,
                    new FlagInfo[]{
                        FlagInfo.of("pages", pageCount),
                        FlagInfo.of("page-index", i),
                        FlagInfo.of("page-bytes", bytes),
                        FlagInfo.of("max-bytes", u)
                    }
                });
                return CheckResult.DENY;
            }
            pages.setTag(i, new NBTString(trimToUtf8Bytes(value, u)));
            changed = true;
        }
        if (pageCount > L || totalBytes > totalMaxBytes) {
            AZ(new Object[]{
                event,
                token,
                new FlagInfo[]{
                    FlagInfo.of("pages", pageCount),
                    FlagInfo.of("max-pages", L),
                    FlagInfo.of("bytes", totalBytes),
                    FlagInfo.of("max-bytes", totalMaxBytes)
                }
            });
            return CheckResult.DENY;
        }
        return changed ? CheckResult.CHANGE : CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private int readInt(String primary, String legacy, int fallback) {
        String primaryPath = kD(new Object[]{primary, 0L});
        if (j_(new Object[0]).contains(primaryPath)) {
            return j_(new Object[0]).getInt(primaryPath, fallback);
        }
        return j_(new Object[0]).getInt(kD(new Object[]{legacy, 0L}), fallback);
    }
    private String trimToUtf8Bytes(String value, int maxBytes) {
        if (value == null || maxBytes < 0) {
            return "";
        }
        if (value.getBytes(StandardCharsets.UTF_8).length <= maxBytes) {
            return value;
        }
        int end = value.length();
        while (end > 0) {
            String candidate = value.substring(0, end);
            if (candidate.getBytes(StandardCharsets.UTF_8).length <= maxBytes) {
                return candidate;
            }
            end--;
        }
        return "";
    }
}
