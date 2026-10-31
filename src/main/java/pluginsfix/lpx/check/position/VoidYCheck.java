package pluginsfix.lpx.check.position;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
public class VoidYCheck extends AbstractCheckT<WrapperPlayClientPlayerFlying> {
    private static final long TELEPORT_COOLDOWN_MS = 2000L;
    private int maxAbsY;
    private long lastCorrectiveTeleportAt;
    public VoidYCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        maxAbsY = j_(new Object[0]).getInt(kD(new Object[]{"max", 0L}), 1_000_000);
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkVoid(args);
    }
    public CheckResult checkVoid(Object[] args) {
        long token = (Long) args[0];
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPlayerFlying packet = (WrapperPlayClientPlayerFlying) args[2];
        if (!packet.hasPositionChanged()) {
            return CheckResult.ALLOW;
        }
        double y = packet.getLocation().getY();
        if (Math.abs(y) <= maxAbsY) {
            return CheckResult.ALLOW;
        }
        teleportDownIfNeeded(protocolPlayer);
        AZ(new Object[]{
            packet,
            token,
            new FlagInfo[]{FlagInfo.of("y", y)}
        });
        return CheckResult.DENY;
    }
    private void teleportDownIfNeeded(ProtocolPlayer protocolPlayer) {
        long now = System.currentTimeMillis();
        if (now - lastCorrectiveTeleportAt < TELEPORT_COOLDOWN_MS) {
            return;
        }
        lastCorrectiveTeleportAt = now;
        protocolPlayer.getPlayer().ifPresent(player ->
            FoliaScheduler.getEntityScheduler().run(
                (Entity) player,
                getOptions().getCheckOptions().getPlugin(),
                task -> applyCorrection(player),
                () -> {}
            )
        );
    }
    private void applyCorrection(Player player) {
        if (player.getLocation().getY() <= maxAbsY) {
            return;
        }
        player.teleport(player.getLocation().clone().subtract(0.0D, maxAbsY - 5, 0.0D));
    }
}
