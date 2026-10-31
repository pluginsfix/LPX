package pluginsfix.lpx.api.check.abstraction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.check.RegisteredCheck;
import pluginsfix.lpx.api.LPXPlugin;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.CheckContainer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public abstract class CheckManager {
    public static final CheckContainer<?> EMPTY_CONTAINER = new CheckContainer<>();
    private final LPXPlugin plugin;
    private final Map<Object, List<RegisteredCheck<?>>> checks = Maps.newHashMap();
    private static int[] legacyState;
    protected CheckManager(LPXPlugin plugin) {
        this.plugin = plugin;
    }
    public void register(Object key, CheckFunction function, CheckType type) {
        checks.computeIfAbsent(key, ignored -> Lists.newArrayList())
            .add(new RegisteredCheck<>(type, new CheckOptions(plugin, type), function));
    }
    public void sortByPriority() {
        checks.values().forEach(list -> list.sort(Comparator.comparingInt(check -> check.getType().getPriority())));
    }
    public Map<Object, CheckContainer<?>> createPlayerChecks(ProtocolPlayer player) {
        Map<Object, CheckContainer<?>> result = new HashMap<>();
        checks.forEach((key, registeredChecks) -> {
            CheckContainer container = new CheckContainer();
            for (RegisteredCheck<?> registered : registeredChecks) {
                CheckFunction function = registered.getFunction();
                container.add(function.apply(player, registered.getOptions()));
            }
            result.put(key, container);
        });
        return result;
    }
    public Map<Object, List<RegisteredCheck<?>>> getChecks() {
        return checks;
    }
    public abstract void registerChecks();
    public static void zb(int[] value) {
        legacyState = value;
    }
    public static int[] aU() {
        return legacyState;
    }
    private static RuntimeException b(RuntimeException runtimeException) {
        return runtimeException;
    }
}
