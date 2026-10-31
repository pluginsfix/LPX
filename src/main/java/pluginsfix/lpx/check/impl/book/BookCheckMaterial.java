package pluginsfix.lpx.check.impl.book;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEditBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class BookCheckMaterial extends AbstractCheckT<WrapperPlayClientEditBook> {
    private static String[] legacyState;
    public BookCheckMaterial(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return handleMaterialCheck(args);
    }
    public CheckResult handleMaterialCheck(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        ItemStack itemInHand = bukkitPlayer.getInventory().getItemInHand();
        if (isBook(itemInHand)) {
            return CheckResult.ALLOW;
        }
        String materialName = itemInHand == null || itemInHand.getType() == null
            ? "null"
            : itemInHand.getType().name();
        AZ(new Object[]{
            args[2],
            0L,
            new FlagInfo[]{FlagInfo.of("material", materialName)}
        });
        return CheckResult.DENY;
    }
    public CheckResult GZ(Object[] args) {
        return handleMaterialCheck(args);
    }
    private boolean isBook(ItemStack item) {
        if (item == null || item.getType() == null) {
            return false;
        }
        String typeName = item.getType().name();
        return typeName.contains("WRITABLE_BOOK") || typeName.contains("WRITTEN_BOOK");
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    public static void setLegacyState(String[] values) {
        legacyState = values;
    }
    public static String[] getLegacyState() {
        return legacyState;
    }
    public static void cr(String[] values) {
        setLegacyState(values);
    }
    public static String[] Y1() {
        return getLegacyState();
    }
}
