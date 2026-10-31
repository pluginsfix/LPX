package pluginsfix.lpx.player;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.check.abstraction.CheckManager;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.CheckResultInfo;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.api.player.PrinterData;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.api.protocol.CheckContainer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import pluginsfix.lpx.core.LPXCore;
import com.github.retrooper.packetevents.protocol.player.User;
import pluginsfix.lpx.printer.PrinterDataImpl;
import pluginsfix.lpx.util.Timer;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
public class ProtocolPlayerImpl implements ProtocolPlayer {
    private static final String BYPASS_PERMISSION = "lpx.bypass";
    private final LPXCore core;
    private final Map<Object, CheckContainer<?>> checks;
    private final User user;
    private final PrinterData printerData;
    @Nullable
    private Player cachedPlayer;
    private boolean exemptPermission;
    private boolean kicked;
    private boolean bedrock;
    private boolean processingPayload;
    private ReceivedPacket lastReceivedPacket;
    private long lastAlert;
    private long lastPunish;
    private long lastTeleport;
    private long lastPortalTeleport;
    private long lastShears;
    private long lastContainerInteract;
    private long packetCount;
    private static boolean legacyState;
    public ProtocolPlayerImpl(LPXCore core, User user, long ignored) {
        this.exemptPermission = false;
        this.kicked = false;
        this.bedrock = false;
        this.processingPayload = false;
        this.packetCount = 0L;
        this.core = core;
        this.user = user;
        this.checks = core.getCheckManager().createPlayerChecks(this);
        this.printerData = new PrinterDataImpl(core, this, 0L);
    }
    @Override
    public Optional<Player> getPlayer() {
        if (cachedPlayer != null && cachedPlayer.isOnline()) {
            return Optional.of(cachedPlayer);
        }
        UUID uuid = user.getUUID();
        if (uuid == null) {
            return Optional.empty();
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            cachedPlayer = null;
            exemptPermission = false;
            return Optional.empty();
        }
        cachedPlayer = player;
        exemptPermission = player.hasPermission(BYPASS_PERMISSION);
        return Optional.of(player);
    }
    @Override
    public User getUser() {
        return user;
    }
    @Override
    public String getUsername() {
        return user.getName();
    }
    @Override
    public UUID getUniqueId() {
        return user.getUUID();
    }
    @Override
    public PrinterData getPrinterData() {
        return printerData;
    }
    @Override
    public boolean isBedrock() {
        return bedrock;
    }
    @Override
    public void setBedrock(boolean bedrock) {
        this.bedrock = bedrock;
    }
    @Override
    public boolean isProcessingPayload() {
        return processingPayload;
    }
    @Override
    public void setProcessingPayload(boolean processingPayload) {
        this.processingPayload = processingPayload;
    }
    public LPXCore mk(Object[] ignored) {
        return core;
    }
    @Override
    public Map<Object, CheckContainer<?>> getChecks() {
        return checks;
    }
    @Override
    public CheckContainer<?> getChecks(Object key) {
        return checks.getOrDefault(key, CheckManager.EMPTY_CONTAINER);
    }
    @Override
    public boolean hasAnyCheck(Object key, boolean preVia) {
        for (Check<?> check : getChecks(key)) {
            if (check.isPreVia() == preVia) {
                return true;
            }
        }
        return false;
    }
    @Override
    public CheckResultInfo handleChecks(Object key, Object payload) {
        return handleChecks(key, payload, false);
    }
    @Override
    public CheckResultInfo handleChecks(Object key, Object payload, boolean preVia) {
        CheckResultInfo resultInfo = new CheckResultInfo(CheckResult.ALLOW);
        for (Check<?> check : getChecks(key)) {
            if (check.isPreVia() != preVia) {
                continue;
            }
            @SuppressWarnings("unchecked")
            Check<Object> typedCheck = (Check<Object>) check;
            CheckResult mergedResult = typedCheck.handle(payload).merge(resultInfo.result());
            if (mergedResult != resultInfo.result()) {
                resultInfo = new CheckResultInfo(mergedResult, check);
            }
            if (mergedResult == CheckResult.DENY) {
                return resultInfo;
            }
        }
        return resultInfo;
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> Check<T> getCheck(Object key, CheckType type, LPX ignored) {
        for (Check<?> check : getChecks(key)) {
            if (check.getType() == type) {
                return (Check<T>) check;
            }
        }
        return null;
    }
    @Override
    public ReceivedPacket getLastReceivedPacket() {
        return lastReceivedPacket;
    }
    @Override
    public long getLastAlert() {
        return lastAlert;
    }
    @Override
    public long getLastPunish() {
        return lastPunish;
    }
    @Override
    public long getLastTeleport() {
        return lastTeleport;
    }
    @Override
    public long getLastPortalTeleport() {
        return lastPortalTeleport;
    }
    @Override
    public long getLastShears() {
        return lastShears;
    }
    @Override
    public long getLastContainerInteract() {
        return lastContainerInteract;
    }
    @Override
    public void setLastReceivedPacket(ReceivedPacket packet) {
        this.lastReceivedPacket = packet;
    }
    @Override
    public void updateLastPunish() {
        this.lastPunish = Timer.Wz(new Object[0]);
    }
    @Override
    public void updateLastAlert() {
        this.lastAlert = Timer.Wz(new Object[0]);
    }
    @Override
    public void updateLastTeleport() {
        this.lastTeleport = Timer.Wz(new Object[0]);
    }
    @Override
    public void updateLastPortalTeleport() {
        this.lastPortalTeleport = Timer.Wz(new Object[0]);
    }
    @Override
    public void updateLastShears() {
        this.lastShears = Timer.Wz(new Object[0]);
    }
    @Override
    public void updateLastContainerInteract() {
        this.lastContainerInteract = Timer.Wz(new Object[0]);
    }
    @Override
    public long incrementPacketCount() {
        return ++packetCount;
    }
    @Override
    public long getPacketCount() {
        return packetCount;
    }
    @Override
    public void flag(Check<?> check, Object ignored, FlagInfo... infos) {
        core.s1(new Object[0]).handleAlert(this, check, infos);
    }
    @Override
    public void punish(Check<?> check) {
        core.s1(new Object[0]).punishPlayer(this, check);
    }
    @Override
    public void kick(String reason) {
        if (isKicked()) {
            return;
        }
        kicked = true;
        core.Fs(new Object[]{this, 0L, reason});
    }
    @Override
    public boolean isKicked() {
        return kicked || getPlayer().isEmpty();
    }
    @Override
    public boolean ignoreChecks() {
        if (bedrock) {
            return true;
        }
        return core.sZ(new Object[0]).Bw(new Object[0]).Yl(new Object[0]) && hasExemptPermission();
    }
    @Override
    public boolean hasExemptPermission() {
        if (cachedPlayer == null) {
            getPlayer();
        }
        return exemptPermission;
    }
    @Override
    public LPX lpx() {
        return core;
    }
    public static void eQ(boolean value) {
        legacyState = value;
    }
    public static boolean me() {
        return legacyState;
    }
    public static boolean md() {
        return !legacyState;
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
