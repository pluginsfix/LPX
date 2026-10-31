package pluginsfix.lpx.check.impl.flood;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.check.RepeatedIntervalCounter;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.check.impl.item.ItemNbtUtils;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import pluginsfix.lpx.player.ProtocolPlayerImpl;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
public class FloodCheckPacketType extends AbstractCheckT<PacketReceiveEvent> {
    private final Map<String, List<PacketTypeRateRule>> rulesByPacket = new HashMap<>();
    private static int X = 59;
    public FloodCheckPacketType(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        rulesByPacket.clear();
        ConfigurationSection limitsSection = j_(new Object[0]).getConfigurationSection(
            kD(new Object[]{"limits", 0L})
        );
        if (limitsSection != null) {
            for (String packetName : limitsSection.getKeys(false)) {
                List<PacketTypeRateRule> rules = parseRulesFromSection(limitsSection, packetName);
                if (!rules.isEmpty()) {
                    rulesByPacket.put(packetName.toUpperCase(Locale.ROOT), rules);
                }
            }
        }
        if (rulesByPacket.isEmpty()) {
            loadLegacyListFormat();
        }
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return K0(args);
    }
    public CheckResult K0(Object[] args) {
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        PacketReceiveEvent event = (PacketReceiveEvent) args[2];
        if (player.getPrinterData().isUsingPrinter()) {
            return CheckResult.ALLOW;
        }
        List<PacketTypeRateRule> rules = rulesByPacket.get(event.getPacketType().getName().toUpperCase(Locale.ROOT));
        if (rules == null || rules.isEmpty()) {
            return CheckResult.ALLOW;
        }
        double weight = Sm(new Object[]{event.getPacketType(), player, 0L});
        boolean shouldCancel = false;
        for (PacketTypeRateRule rule : rules) {
            if (!rule.getCounter().increment(weight)) {
                continue;
            }
            AZ(new Object[]{
                event,
                0L,
                new FlagInfo[]{
                    FlagInfo.of("weight", weight),
                    FlagInfo.of("max", rule.getCounter().getMaxCounter()),
                    FlagInfo.of("interval", rule.getCounter().getInterval() + "ms"),
                    FlagInfo.of("periods", rule.getCounter().getMaxPeriods()),
                    FlagInfo.of("warnings", rule.getCounter().getMaxWarnings())
                }
            });
            if (rule.isCountFailedOnly()) {
                shouldCancel = true;
            }
        }
        return shouldCancel ? CheckResult.DENY : CheckResult.ALLOW;
    }
    private double Sm(Object[] args) {
        PacketTypeCommon type = (PacketTypeCommon) args[0];
        ProtocolPlayer player = (ProtocolPlayer) args[1];
        if (type == PacketType.Play.Client.CLICK_WINDOW || type == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION) {
            if (ItemNbtUtils.uo(new Object[]{0L, player})) {
                return 1.25D;
            }
            if (ItemNbtUtils.uB(new Object[]{player, 0L})) {
                return 0.5D;
            }
        }
        return 1.0D;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private List<PacketTypeRateRule> parseRulesFromSection(ConfigurationSection limitsSection, String packetName) {
        List<PacketTypeRateRule> rules = new ArrayList<>();
        if (limitsSection.isList(packetName)) {
            for (Map<?, ?> map : limitsSection.getMapList(packetName)) {
                PacketTypeRateRule rule = parseRuleMap(map);
                if (rule != null) {
                    rules.add(rule);
                }
            }
            return rules;
        }
        ConfigurationSection ruleSection = limitsSection.getConfigurationSection(packetName);
        if (ruleSection != null) {
            PacketTypeRateRule rule = parseRuleSection(ruleSection);
            if (rule != null) {
                rules.add(rule);
            }
        }
        return rules;
    }
    private PacketTypeRateRule parseRuleSection(ConfigurationSection section) {
        int max = section.getInt("max", 20);
        int interval = section.getInt("interval", 1000);
        int periods = section.getInt("periods", 5);
        int warnings = section.getInt("warnings", 2);
        boolean cancel = "drop".equalsIgnoreCase(section.getString("action", "flag"));
        return new PacketTypeRateRule(new RepeatedIntervalCounter(max, interval, periods, warnings), cancel);
    }
    private PacketTypeRateRule parseRuleMap(Map<?, ?> map) {
        int max = getInt(map, "max", 20);
        int interval = getInt(map, "interval", 1000);
        int periods = getInt(map, "periods", 5);
        int warnings = getInt(map, "warnings", 2);
        Object actionValue = map.containsKey("action") ? map.get("action") : "flag";
        String action = String.valueOf(actionValue);
        boolean cancel = "drop".equalsIgnoreCase(action) || "deny".equalsIgnoreCase(action) || "cancel".equalsIgnoreCase(action);
        return new PacketTypeRateRule(new RepeatedIntervalCounter(max, interval, periods, warnings), cancel);
    }
    private int getInt(Map<?, ?> map, String key, int fallback) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }
    private void loadLegacyListFormat() {
        List<String> rawRules = j_(new Object[0]).getStringList(kD(new Object[]{"rules", 0L}));
        for (String line : rawRules) {
            String[] split = line.split(",");
            if (split.length < 2) {
                logParseWarning(() -> "Invalid flood rule: " + line + " (expected packet,max[,interval,periods,warnings,action])");
                continue;
            }
            try {
                String packetName = split[0].trim().toUpperCase(Locale.ROOT);
                int max = Integer.parseInt(split[1].trim());
                int interval = split.length > 2 ? Integer.parseInt(split[2].trim()) : 1000;
                int periods = split.length > 3 ? Integer.parseInt(split[3].trim()) : 5;
                int warnings = split.length > 4 ? Integer.parseInt(split[4].trim()) : 2;
                String action = split.length > 5 ? split[5].trim() : "flag";
                boolean cancel = "drop".equalsIgnoreCase(action) || "deny".equalsIgnoreCase(action) || "cancel".equalsIgnoreCase(action);
                PacketTypeRateRule rule = new PacketTypeRateRule(
                    new RepeatedIntervalCounter(max, interval, periods, warnings),
                    cancel
                );
                rulesByPacket.computeIfAbsent(packetName, ignored -> new ArrayList<>()).add(rule);
            } catch (NumberFormatException numberFormatException) {
                logParseWarning(() -> "Invalid numeric flood rule: " + line);
            }
        }
    }
    private void logParseWarning(Supplier<String> supplier) {
        if (F instanceof ProtocolPlayerImpl) {
            ((ProtocolPlayerImpl) F).mk(new Object[0]).getLogger().warning(supplier.get());
        }
    }
    public static void EI(int value) {
        X = value;
    }
    public static int jM() {
        return X;
    }
    public static int jP() {
        return X == 0 ? 59 : 0;
    }
}
