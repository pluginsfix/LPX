package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import pluginsfix.lpx.util.ChannelUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
public class ItemCheckBlockedNBT extends AbstractCheckT<ItemStackDecodeEvent> {
    private List<String> blockedTags = new ArrayList<>();
    public ItemCheckBlockedNBT(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        FileConfiguration config = j_(new Object[0]);
        blockedTags = readStringList(config, "blocked-tags", "blacklist", "tags");
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return yq(new Object[]{player, token, event});
    }
    public CheckResult yq(Object[] args) {
        long token = (Long) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        ItemType type = itemStack.getType();
        if (type != ItemTypes.BOOK && type != ItemTypes.WRITABLE_BOOK && type != ItemTypes.WRITTEN_BOOK) {
            return CheckResult.ALLOW;
        }
        NBTCompound nbt = itemStack.getNBT();
        if (nbt == null || blockedTags.isEmpty()) {
            return CheckResult.ALLOW;
        }
        for (String blockedTag : blockedTags) {
            if (!ChannelUtils.aM(new Object[]{nbt, blockedTag, token})) {
                continue;
            }
            AZ(new Object[]{
                event,
                token,
                new FlagInfo[]{
                    FlagInfo.of("tag", blockedTag)
                }
            });
            return CheckResult.DENY;
        }
        return CheckResult.ALLOW;
    }
    private List<String> readStringList(FileConfiguration config, String... keys) {
        for (String key : keys) {
            String path = kD(new Object[]{key, 0L});
            if (config.contains(path)) {
                return new ArrayList<>(config.getStringList(path));
            }
        }
        return new ArrayList<>();
    }
}
