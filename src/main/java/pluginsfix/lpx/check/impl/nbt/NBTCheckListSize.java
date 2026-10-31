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
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
public class NBTCheckListSize extends AbstractCheckT<NBTDecodeEvent> {
    private int U;
    private int S;
    public NBTCheckListSize(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        U = readInt("max-legacy", "max", 50);
        S = readInt("max-modern", "max-1-14+", 100);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        return EZ(new Object[]{player, token, event});
    }
    public CheckResult EZ(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[0];
        long token = (Long) args[1];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        NBT nbt = event.getNbt();
        if (!(nbt instanceof NBTCompound compound)) {
            return CheckResult.ALLOW;
        }
        NBTList<NBTString> list = compound.getStringListTagOrNull("Lore");
        if (list == null) {
            return CheckResult.ALLOW;
        }
        boolean modernClient = player.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
        int limit = modernClient ? S : U;
        if (list.size() <= limit) {
            return CheckResult.ALLOW;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("size", list.size()),
                FlagInfo.of("limit", limit),
                FlagInfo.of("modern-client", modernClient)
            }
        });
        return CheckResult.DENY;
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
}
