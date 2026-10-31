package pluginsfix.lpx.api.player;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.check.CheckResultInfo;
import pluginsfix.lpx.api.check.enums.CheckType;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.LPX;
import pluginsfix.lpx.api.protocol.CheckContainer;
import pluginsfix.lpx.api.protocol.ReceivedPacket;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
public interface ProtocolPlayer {
    public Optional<Player> getPlayer();
    public User getUser();
    public String getUsername();
    public UUID getUniqueId();
    public PrinterData getPrinterData();
    public boolean isBedrock();
    public void setBedrock(boolean var1);
    public boolean isProcessingPayload();
    public void setProcessingPayload(boolean var1);
    public Map<Object, CheckContainer<?>> getChecks();
    public CheckContainer<?> getChecks(Object var1);
    public boolean hasAnyCheck(Object var1, boolean var2);
    public CheckResultInfo handleChecks(Object var1, Object var2);
    public CheckResultInfo handleChecks(Object var1, Object var2, boolean var3);
    public <T> Check<T> getCheck(Object var1, CheckType var2, LPX var3);
    public ReceivedPacket getLastReceivedPacket();
    public long getLastAlert();
    public long getLastPunish();
    public long getLastTeleport();
    public void updateLastAlert();
    public long getLastPortalTeleport();
    public long getLastShears();
    public long getLastContainerInteract();
    public void setLastReceivedPacket(ReceivedPacket var1);
    public void updateLastPunish();
    public void updateLastTeleport();
    public void updateLastPortalTeleport();
    public void updateLastShears();
    public void updateLastContainerInteract();
    public long incrementPacketCount();
    public long getPacketCount();
    public void flag(Check<?> var1, Object var2, FlagInfo ... var3);
    public void punish(Check<?> var1);
    public void kick(String var1);
    public boolean isKicked();
    public boolean ignoreChecks();
    public boolean hasExemptPermission();
    default public boolean usesComponents() {
        return getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5);
    }
    default public boolean usesHashedStacks() {
        return getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5);
    }
    public LPX lpx();
}
