package pluginsfix.lpx.check.impl.nbt;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.support.GameModeFilter;
import com.github.retrooper.packetevents.event.simple.NBTDecodeEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
public class NBTCheckCreativeGameMode extends AbstractCheckT<NBTDecodeEvent> {
    private GameModeFilter d = GameModeFilter.DEFAULT;
    private int maxStringLength;
    public NBTCheckCreativeGameMode(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        String mode = getOptions().getCheckOptions().getConfig().getString(
            kD(new Object[]{"mode", 0L}),
            GameModeFilter.DEFAULT.name()
        );
        d = GameModeFilter.si(new Object[]{mode});
        maxStringLength = j_(new Object[0]).getInt(kD(new Object[]{"max-string-length", 0L}), 8192);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        return kg(new Object[]{player, token, event});
    }
    public CheckResult kg(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[0];
        long token = (Long) args[1];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        Player player = protocolPlayer.getPlayer().orElse(null);
        if (player == null || isAllowedMode(player.getGameMode())) {
            return CheckResult.ALLOW;
        }
        return N_(new Object[]{event, token, Boolean.TRUE});
    }
    @SuppressWarnings("unchecked")
    private CheckResult N_(Object[] args) {
        NBTDecodeEvent event = (NBTDecodeEvent) args[0];
        NBT nbt = event.getNbt();
        if (nbt instanceof NBTCompound) {
            return CheckResult.ALLOW;
        }
        if (nbt instanceof NBTString stringTag) {
            if (stringTag.getValue().length() <= maxStringLength) {
                return CheckResult.ALLOW;
            }
            event.setNbt(new NBTString(stringTag.getValue().substring(0, maxStringLength)));
            return CheckResult.CHANGE;
        }
        if (nbt instanceof NBTList<?> list) {
            boolean changed = false;
            for (int i = 0; i < list.size(); i++) {
                NBT value = list.getTag(i);
                if (!(value instanceof NBTString stringTag)) {
                    list.removeTag(i--);
                    changed = true;
                    continue;
                }
                if (stringTag.getValue().length() <= maxStringLength) {
                    continue;
                }
                list.removeTag(i);
                list.addTagUnsafe(i, new NBTString(stringTag.getValue().substring(0, maxStringLength)));
                changed = true;
            }
            if (changed) {
                event.setNbt(list);
                return CheckResult.CHANGE;
            }
            return CheckResult.ALLOW;
        }
        event.setNbt(new NBTCompound());
        return CheckResult.CHANGE;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private boolean isAllowedMode(GameMode mode) {
        switch (d) {
            case NONE:
                return false;
            case ALL:
                return true;
            case CREATIVE:
                return mode == GameMode.CREATIVE;
            case SMART:
            default:
                return mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR;
        }
    }
}
