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
public class NBTCheckUnicode extends AbstractCheckT<NBTDecodeEvent> {
    private boolean t;
    public NBTCheckUnicode(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        NBTDecodeEvent event = (NBTDecodeEvent) args[2];
        return Vd(new Object[]{player, token, event});
    }
    private static String bF(Object[] args) {
        String value = (String) args[1];
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder cleaned = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (cd(new Object[]{(int) c, 0L})) {
                cleaned.append(c);
            }
        }
        return cleaned.toString();
    }
    private static boolean cd(Object[] args) {
        int codePoint = (Integer) args[0];
        if (codePoint < 32) {
            return false;
        }
        int type = Character.getType((char) codePoint);
        return type != Character.UNASSIGNED
            && type != Character.CONTROL
            && type != Character.PRIVATE_USE
            && type != Character.SURROGATE;
    }
    private static boolean bO(Object[] args) {
        String value = args.length > 1 ? (String) args[1] : (String) args[0];
        if (value == null || value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!cd(new Object[]{(int) value.charAt(i), 0L})) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void loadOptions() {
        t = j_(new Object[0]).getBoolean(kD(new Object[]{"sanitize", 0L}),
            j_(new Object[0]).getBoolean(kD(new Object[]{"strip-invalid", 0L}), false));
    }
    public CheckResult Vd(Object[] args) {
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
        boolean changed = false;
        for (int i = 0; i < list.size(); i++) {
            NBTString page = list.getTag(i);
            String value = page.getValue();
            if (!bO(new Object[]{token, value})) {
                continue;
            }
            if (!t) {
                AZ(new Object[]{
                    event,
                    token,
                    new FlagInfo[]{
                        FlagInfo.of("pages", list.size()),
                        FlagInfo.of("index", i),
                        FlagInfo.of("length", value.length())
                    }
                });
                return CheckResult.DENY;
            }
            list.setTag(i, new NBTString(bF(new Object[]{token, value})));
            changed = true;
        }
        return changed ? CheckResult.CHANGE : CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
}
