package pluginsfix.lpx.check.impl.nbt;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.NBTDecodeEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
public class NBTCheckListFilter extends AbstractCheckT<NBTDecodeEvent> {
    private boolean t;
    public NBTCheckListFilter(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        t = j_(new Object[0]).getBoolean(kD(new Object[]{"sanitize", 0L}),
            j_(new Object[0]).getBoolean(kD(new Object[]{"replace-null-chars", 0L}), true));
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        return zp(new Object[]{player, token, event});
    }
    public CheckResult zp(Object[] args) {
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        NBT nbt = event.getNbt();
        if (!(nbt instanceof NBTCompound compound)) {
            return CheckResult.ALLOW;
        }
        NBTList<NBTString> list = compound.getStringListTagOrNull("Lore");
        if (list == null || !t) {
            return CheckResult.ALLOW;
        }
        boolean changed = false;
        for (int i = 0; i < list.size(); i++) {
            NBTString value = list.getTag(i);
            String filtered = value.getValue().replace('\u0000', ' ');
            if (filtered.equals(value.getValue())) {
                continue;
            }
            list.setTag(i, new NBTString(filtered));
            changed = true;
        }
        return changed ? CheckResult.CHANGE : CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
