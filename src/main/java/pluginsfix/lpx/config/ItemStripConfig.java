package pluginsfix.lpx.config;
import pluginsfix.lpx.api.LPXPlugin;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public class ItemStripConfig extends ConfigSection {
    private final boolean enabled;
    private final boolean setSlot;
    private final Set<String> removeComponents;
    private final Set<String> removeNbt;
    public ItemStripConfig(long ignored, LPXPlugin plugin, String pathPrefix, MessagesConfig messages) {
        super(plugin, pathPrefix + ".item-strip", messages);
        this.enabled = ln(new Object[]{"enabled"});
        this.setSlot = ln(new Object[]{"set-slot"});
        this.removeComponents = normalizeSet(lt(new Object[]{0L, "remove-components"}));
        this.removeNbt = normalizeSet(lt(new Object[]{0L, "remove-nbt"}));
    }
    private Set<String> normalizeSet(Iterable<String> values) {
        Set<String> normalized = new HashSet<>();
        for (String value : values) {
            if (value == null) {
                continue;
            }
            String trimmed = value.trim();
            if (!trimmed.isEmpty()) {
                normalized.add(trimmed);
            }
        }
        return Collections.unmodifiableSet(normalized);
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isSetSlot() {
        return setSlot;
    }
    public Set<String> getRemoveComponents() {
        return removeComponents;
    }
    public Set<String> getRemoveNbt() {
        return removeNbt;
    }
    public boolean Nn(Object[] ignored) {
        return isEnabled();
    }
    public boolean NR(Object[] ignored) {
        return isSetSlot();
    }
    public Set Nz(Object[] ignored) {
        return getRemoveComponents();
    }
    public Set N5(Object[] ignored) {
        return getRemoveNbt();
    }
}
