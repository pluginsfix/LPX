package pluginsfix.lpx.check.impl.item;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.support.GameModeFilter;
import com.github.retrooper.packetevents.event.simple.ItemStackDecodeEvent;
import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
public class ItemCheckComponents extends AbstractCheckT<ItemStackDecodeEvent> {
    private GameModeFilter q = GameModeFilter.DEFAULT;
    private Set<String> N = new HashSet<>();
    private boolean stripBlockedComponents;
    public ItemCheckComponents(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        FileConfiguration config = getOptions().getCheckOptions().getConfig();
        String mode = config.getString(kD(new Object[]{"mode", 0L}), null);
        if (mode == null) {
            mode = config.getString(kD(new Object[]{"gamemode", 0L}), GameModeFilter.DEFAULT.name());
        }
        q = GameModeFilter.si(new Object[]{mode});
        N = new HashSet<>(readStringList(config, "blocked", "components", "blacklist"));
        stripBlockedComponents = config.getBoolean(kD(new Object[]{"strip", 0L}),
            config.getBoolean(kD(new Object[]{"remove", 0L}), false));
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        long token = (Long) args[0];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        return yx(new Object[]{player, token, event});
    }
    @SuppressWarnings("unchecked")
    public CheckResult yx(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[0];
        long token = (Long) args[1];
        ItemStackDecodeEvent event = (ItemStackDecodeEvent) args[2];
        if (!protocolPlayer.usesComponents()) {
            return CheckResult.ALLOW;
        }
        if (N.isEmpty()) {
            return CheckResult.ALLOW;
        }
        Player player = protocolPlayer.getPlayer().orElse(null);
        if (player == null || !shouldCheckForMode(player.getGameMode())) {
            return CheckResult.ALLOW;
        }
        ItemStack itemStack = event.getItemStack();
        if (itemStack == null || itemStack.getComponents() == null) {
            return CheckResult.ALLOW;
        }
        Map<ComponentType<?>, ?> patches;
        try {
            patches = (Map<ComponentType<?>, ?>) itemStack.getComponents().getPatches();
        } catch (Throwable throwable) {
            return CheckResult.ALLOW;
        }
        if (patches == null || patches.isEmpty()) {
            return CheckResult.ALLOW;
        }
        List<String> blocked = new ArrayList<>();
        for (ComponentType<?> type : patches.keySet()) {
            if (type == null || type.getName() == null) {
                continue;
            }
            if (lambda$check$0(type)) {
                blocked.add(type.getName().getKey());
            }
        }
        if (blocked.isEmpty()) {
            return CheckResult.ALLOW;
        }
        if (stripBlockedComponents) {
            patches.keySet().removeIf(this::lambda$check$0);
            return CheckResult.CHANGE;
        }
        AZ(new Object[]{
            event,
            token,
            new FlagInfo[]{
                FlagInfo.of("blocked", String.join(", ", blocked)),
                FlagInfo.of("count", blocked.size())
            }
        });
        return CheckResult.DENY;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private boolean lambda$check$0(ComponentType<?> componentType) {
        return componentType != null
            && componentType.getName() != null
            && N.contains(componentType.getName().getKey());
    }
    private boolean shouldCheckForMode(GameMode gameMode) {
        switch (q) {
            case NONE:
                return false;
            case ALL:
                return true;
            case CREATIVE:
                return gameMode == GameMode.CREATIVE;
            case SMART:
            default:
                return gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR;
        }
    }
    private List<String> readStringList(FileConfiguration config, String... keys) {
        for (String key : keys) {
            String path = kD(new Object[]{key, 0L});
            if (config.contains(path)) {
                return config.getStringList(path);
            }
        }
        return List.of();
    }
}
