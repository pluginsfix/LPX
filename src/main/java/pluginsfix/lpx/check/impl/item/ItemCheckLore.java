package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import com.github.retrooper.packetevents.protocol.component.builtin.item.WrittenBookContent;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.util.Filterable;
import net.kyori.adventure.text.Component;
public class ItemCheckLore extends AbstractCheckT<ItemStackDecodeEvent> {
    private int l;
    private int H;
    public ItemCheckLore(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        l = readInt("max-depth", "max-component-depth", 32);
        H = readInt("max-length", "max-text-length", 8192);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return hW(new Object[]{player, token, event});
    }
    public CheckResult hW(Object[] args) {
        long token = (Long) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            return CheckResult.ALLOW;
        }
        if (F.usesComponents()) {
            CheckResult result = inspectComponent("custom-name", itemStack.getComponentOr(ComponentTypes.CUSTOM_NAME, null), event, token);
            if (result == CheckResult.DENY) {
                return result;
            }
            result = inspectComponent("item-name", itemStack.getComponentOr(ComponentTypes.ITEM_NAME, null), event, token);
            if (result == CheckResult.DENY) {
                return result;
            }
            ItemLore lore = itemStack.getComponentOr(ComponentTypes.LORE, null);
            if (lore != null) {
                for (int i = 0; i < lore.getLines().size(); i++) {
                    result = inspectComponent("lore[" + i + "]", lore.getLines().get(i), event, token);
                    if (result == CheckResult.DENY) {
                        return result;
                    }
                }
            }
            WrittenBookContent bookContent = itemStack.getComponentOr(ComponentTypes.WRITTEN_BOOK_CONTENT, null);
            if (bookContent != null) {
                for (int i = 0; i < bookContent.getPages().size(); i++) {
                    Filterable<Component> page = bookContent.getPages().get(i);
                    result = inspectComponent("book-raw[" + i + "]", page.getRaw(), event, token);
                    if (result == CheckResult.DENY) {
                        return result;
                    }
                    if (page.getFiltered() != null) {
                        result = inspectComponent("book-filtered[" + i + "]", page.getFiltered(), event, token);
                        if (result == CheckResult.DENY) {
                            return result;
                        }
                    }
                }
            }
            return CheckResult.ALLOW;
        }
        NBTCompound nbt = itemStack.getNBT();
        if (nbt == null) {
            return CheckResult.ALLOW;
        }
        NBTCompound display = nbt.getCompoundTagOrNull("display");
        if (display == null) {
            return CheckResult.ALLOW;
        }
        String name = display.getStringTagValueOrNull("Name");
        if (name != null && name.length() > H) {
            return denyFor("display.name", name.length(), 1, event, token);
        }
        NBTList<NBTString> lore = display.getStringListTagOrNull("Lore");
        if (lore == null) {
            return CheckResult.ALLOW;
        }
        for (int i = 0; i < lore.size(); i++) {
            String value = lore.getTag(i).getValue();
            if (value.length() > H) {
                return denyFor("display.lore[" + i + "]", value.length(), 1, event, token);
            }
        }
        return CheckResult.ALLOW;
    }
    private CheckResult Zr(Object[] args) {
        Component component = (Component) args[2];
        String path = (String) args[3];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[4];
        return inspectComponent(path, component, event, 0L);
    }
    private boolean Ll(Object[] args) {
        Component component = (Component) args[0];
        if (component == null) {
            return false;
        }
        int[] metrics = collectMetrics(component);
        int length = String.valueOf(component).length();
        return metrics[0] > l || length > H;
    }
    private void Oi(Object[] args) {
        int[] metrics = (int[]) args[0];
        Component component = (Component) args[1];
        int depth = (Integer) args[2];
        if (component == null) {
            return;
        }
        metrics[0] = Math.max(metrics[0], depth);
        metrics[1] += 1;
        for (Component child : component.children()) {
            Oi(new Object[]{metrics, child, depth + 1});
        }
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private CheckResult inspectComponent(String path, Component component, ItemStackDecodeEvent event, long token) {
        if (component == null) {
            return CheckResult.ALLOW;
        }
        int[] metrics = collectMetrics(component);
        int depth = metrics[0];
        int length = String.valueOf(component).length();
        if (depth <= l && length <= H) {
            return CheckResult.ALLOW;
        }
        return denyFor(path, length, depth, event, token);
    }
    private int[] collectMetrics(Component component) {
        int[] metrics = new int[]{0, 0};
        Oi(new Object[]{metrics, component, 1});
        return metrics;
    }
    private CheckResult denyFor(String path, int length, int depth, ItemStackDecodeEvent event, long token) {
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("path", path),
                FlagInfo.of("length", length),
                FlagInfo.of("max-length", H),
                FlagInfo.of("depth", depth),
                FlagInfo.of("max-depth", l)
            }
        });
        return CheckResult.DENY;
    }
    private int readInt(String primary, String legacy, int fallback) {
        String primaryPath = kD(new Object[]{primary, 0L});
        if (j_(new Object[0]).contains(primaryPath)) {
            return j_(new Object[0]).getInt(primaryPath, fallback);
        }
        return j_(new Object[0]).getInt(kD(new Object[]{legacy, 0L}), fallback);
    }
}
