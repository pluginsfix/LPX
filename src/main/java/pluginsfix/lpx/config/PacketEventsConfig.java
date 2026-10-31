package pluginsfix.lpx.config;

import pluginsfix.lpx.api.LPXPlugin;
import com.github.retrooper.packetevents.PacketEvents;

public class PacketEventsConfig extends ConfigSection {
    private static int legacyState;

    private final boolean silentFailures;
    private final boolean alertsOnJoin;
    private final boolean kickOnException;
    private final int maxItemDepth;
    private final int maxItemsPerPacket;
    private final boolean hiddenCommand;
    private final boolean bypassPermission;
    private final boolean geyserSupport;
    private final boolean axiomSupport;
    private final boolean checkUpdates;
    private final boolean bStatsEnabled;
    private final boolean clearTaskEnabled;
    private final int clearTaskDelaySec;
    private final PrinterConfig printerConfig;
    private final ItemStripConfig itemStripConfig;
    private final int netherPortalDelayMs;
    private final boolean netherPortalDelayEnabled;
    private final int maxArrowVelocity;
    private final boolean maxArrowVelocityEnabled;
    private final int shearsCooldownMs;
    private final boolean shearsCooldownEnabled;
    private final boolean breakCloseInventory;
    private final boolean trapdoorRailRedstone;
    private final boolean interactContainerDelayEnabled;
    private final long interactContainerDelayMs;
    private final boolean alertsStore;
    private final int alertsRetentionDays;
    private final String serverName;
    private final int punishDelayMs;
    private final boolean debug;
    private final boolean logCancelledPackets;

    public PacketEventsConfig(MessagesConfig messagesConfig, LPXPlugin plugin, long ignored) {
        super(plugin, "options", messagesConfig);

        this.silentFailures = readBoolean("silent-failures", false);

        this.alertsOnJoin = readBoolean("alerts-on-join", true);

        this.kickOnException = readBoolean("kick-on-exception", true);
        PacketEvents.getAPI().getSettings().kickOnPacketException(kickOnException);

        this.maxItemDepth = readInt("max-item-depth", 4);

        this.maxItemsPerPacket = readInt("max-items-per-packet", 1458);

        this.hiddenCommand = readBoolean("hidden-command", false);
        this.bypassPermission = readBoolean("bypass-permission", false);
        this.geyserSupport = readBoolean("geyser", false);
        this.axiomSupport = readBoolean("axiom", false);

        this.checkUpdates = readBoolean("check-updates", true);
        this.bStatsEnabled = readBoolean("bstats", true);
        this.clearTaskEnabled = readBoolean("clear-task.enabled", true);
        this.clearTaskDelaySec = readInt("clear-task.delay", 300);

        this.printerConfig = new PrinterConfig(0L, plugin, "options", messagesConfig);
        this.itemStripConfig = new ItemStripConfig(0L, plugin, "options", messagesConfig);

        this.netherPortalDelayMs = readInt("mechanics.nether-portal-delay", 1000);
        this.netherPortalDelayEnabled = netherPortalDelayMs != -1;

        this.maxArrowVelocity = readInt("mechanics.max-arrow-velocity", 15);
        this.maxArrowVelocityEnabled = maxArrowVelocity != -1;

        this.shearsCooldownMs = readInt("mechanics.shears-cooldown", 500);
        this.shearsCooldownEnabled = shearsCooldownMs != -1;

        this.breakCloseInventory = readBoolean("mechanics.break-close-inventory", false);
        this.trapdoorRailRedstone = readBoolean("mechanics.trapdoor-rail-redstone", true);

        this.interactContainerDelayMs = readInt("mechanics.interact-container-delay", 100);
        this.interactContainerDelayEnabled = interactContainerDelayMs != -1;

        this.alertsStore = readBoolean("alerts.store", true);
        this.alertsRetentionDays = readInt("alerts.days", 14);
        this.serverName = readString("server", "unnamed");
        this.punishDelayMs = readInt("punish-delay", 1000);
        this.debug = readBoolean("debug", false);
        this.logCancelledPackets = readBoolean("log-cancelled-packets", true);
    }

    private boolean readBoolean(String key, boolean fallback) {
        return sl(new Object[0]).getBoolean(i + "." + key, fallback);
    }

    private int readInt(String key, int fallback) {
        return sl(new Object[0]).getInt(i + "." + key, fallback);
    }

    private String readString(String key, String fallback) {
        return sl(new Object[0]).getString(i + "." + key, fallback);
    }

    public boolean isSilentFailuresEnabled() {
        return silentFailures;
    }

    public boolean isAlertsOnJoinEnabled() {
        return alertsOnJoin;
    }

    public boolean isKickOnExceptionEnabled() {
        return kickOnException;
    }

    public int getMaxItemDepth() {
        return maxItemDepth;
    }

    public int getMaxItemsPerPacket() {
        return maxItemsPerPacket;
    }

    public boolean isHiddenCommandEnabled() {
        return hiddenCommand;
    }

    public boolean isBypassPermissionEnabled() {
        return bypassPermission;
    }

    public boolean isGeyserSupportEnabled() {
        return geyserSupport;
    }

    public boolean isAxiomSupportEnabled() {
        return axiomSupport;
    }

    public boolean isCheckUpdatesEnabled() {
        return checkUpdates;
    }

    public boolean isBStatsEnabled() {
        return bStatsEnabled;
    }

    public boolean isClearTaskEnabled() {
        return clearTaskEnabled;
    }

    public int getClearTaskDelaySec() {
        return clearTaskDelaySec;
    }

    public PrinterConfig getPrinterConfig() {
        return printerConfig;
    }

    public ItemStripConfig getItemStripConfig() {
        return itemStripConfig;
    }

    public int getNetherPortalDelayMs() {
        return netherPortalDelayMs;
    }

    public boolean isNetherPortalDelayEnabled() {
        return netherPortalDelayEnabled;
    }

    public int getMaxArrowVelocity() {
        return maxArrowVelocity;
    }

    public boolean isMaxArrowVelocityEnabled() {
        return maxArrowVelocityEnabled;
    }

    public int getShearsCooldownMs() {
        return shearsCooldownMs;
    }

    public boolean isShearsCooldownEnabled() {
        return shearsCooldownEnabled;
    }

    public boolean isBreakCloseInventoryEnabled() {
        return breakCloseInventory;
    }

    public boolean isTrapdoorRailRedstoneEnabled() {
        return trapdoorRailRedstone;
    }

    public boolean isInteractContainerDelayEnabled() {
        return interactContainerDelayEnabled;
    }

    public long getInteractContainerDelayMs() {
        return interactContainerDelayMs;
    }

    public boolean isAlertsStoreEnabled() {
        return alertsStore;
    }

    public int getAlertsRetentionDays() {
        return alertsRetentionDays;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPunishDelayMs() {
        return punishDelayMs;
    }

    public boolean isDebugEnabled() {
        return debug;
    }

    public boolean isLogCancelledPacketsEnabled() {
        return logCancelledPackets;
    }

    public static void setLegacyStateValue(int value) {
        legacyState = value;
    }

    public static int getLegacyStateValue() {
        return legacyState;
    }

    public static int getLegacyFallbackValue() {
        return getLegacyStateValue() == 0 ? 98 : 0;
    }

    public boolean Y_(Object[] ignored) {
        return isSilentFailuresEnabled();
    }

    public boolean YG(Object[] ignored) {
        return isAlertsOnJoinEnabled();
    }

    public boolean YF(Object[] ignored) {
        return isKickOnExceptionEnabled();
    }

    public int Yo(Object[] ignored) {
        return getMaxItemDepth();
    }

    public int Yy(Object[] ignored) {
        return getMaxItemsPerPacket();
    }

    public boolean Yr(Object[] ignored) {
        return isHiddenCommandEnabled();
    }

    public boolean Yl(Object[] ignored) {
        return isBypassPermissionEnabled();
    }

    public boolean YJ(Object[] ignored) {
        return isGeyserSupportEnabled();
    }

    public boolean YU(Object[] ignored) {
        return isAxiomSupportEnabled();
    }

    public boolean Yh(Object[] ignored) {
        return isCheckUpdatesEnabled();
    }

    public boolean Ys(Object[] ignored) {
        return isBStatsEnabled();
    }

    public boolean Yp(Object[] ignored) {
        return isClearTaskEnabled();
    }

    public int Y9(Object[] ignored) {
        return getClearTaskDelaySec();
    }

    public PrinterConfig Y1(Object[] ignored) {
        return getPrinterConfig();
    }

    public ItemStripConfig YT(Object[] ignored) {
        return getItemStripConfig();
    }

    public int YS(Object[] ignored) {
        return getNetherPortalDelayMs();
    }

    public boolean Y5(Object[] ignored) {
        return isNetherPortalDelayEnabled();
    }

    public int Y6(Object[] ignored) {
        return getMaxArrowVelocity();
    }

    public boolean Ya(Object[] ignored) {
        return isMaxArrowVelocityEnabled();
    }

    public int Yz(Object[] ignored) {
        return getShearsCooldownMs();
    }

    public boolean YA(Object[] ignored) {
        return isShearsCooldownEnabled();
    }

    public boolean YB(Object[] ignored) {
        return isBreakCloseInventoryEnabled();
    }

    public boolean Yn(Object[] ignored) {
        return isTrapdoorRailRedstoneEnabled();
    }

    public boolean Y8(Object[] ignored) {
        return isInteractContainerDelayEnabled();
    }

    public long Yf(Object[] ignored) {
        return getInteractContainerDelayMs();
    }

    public boolean YI(Object[] ignored) {
        return isAlertsStoreEnabled();
    }

    public int YD(Object[] ignored) {
        return getAlertsRetentionDays();
    }

    public String YQ(Object[] ignored) {
        return getServerName();
    }

    public int YK(Object[] ignored) {
        return getPunishDelayMs();
    }

    public boolean Yk(Object[] ignored) {
        return isDebugEnabled();
    }

    public boolean YR(Object[] ignored) {
        return isLogCancelledPacketsEnabled();
    }

    public static void Vo(int value) {
        setLegacyStateValue(value);
    }

    public static int Y4() {
        return getLegacyStateValue();
    }

    public static int YY() {
        return getLegacyFallbackValue();
    }
}
