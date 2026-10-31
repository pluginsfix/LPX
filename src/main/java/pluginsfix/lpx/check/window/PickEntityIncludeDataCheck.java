package pluginsfix.lpx.check.window;
import pluginsfix.lpx.check.base.AbstractCheckT;
import pluginsfix.lpx.api.check.CheckResult;
import pluginsfix.lpx.api.check.options.CheckOptions;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import pluginsfix.lpx.check.support.GameModeFilter;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPickItemFromEntity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
public class PickEntityIncludeDataCheck extends AbstractCheckT<WrapperPlayClientPickItemFromEntity> {
    private GameModeFilter gameModeFilter = GameModeFilter.DEFAULT;
    public PickEntityIncludeDataCheck(ProtocolPlayer player, CheckOptions options) {
        super(player, options);
    }
    @Override
    public void loadOptions() {
        FileConfiguration config = getOptions().getCheckOptions().getConfig();
        String mode = config.getString(kD(new Object[]{"mode", 0L}), null);
        if (mode == null) {
            mode = config.getString(kD(new Object[]{"gamemode", 0L}), GameModeFilter.DEFAULT.name());
        }
        gameModeFilter = GameModeFilter.si(new Object[]{mode});
    }
    @Override
    protected CheckResult Bu(Object[] args) {
        return checkPickFromEntity(args);
    }
    public CheckResult checkPickFromEntity(Object[] args) {
        ProtocolPlayer protocolPlayer = (ProtocolPlayer) args[1];
        WrapperPlayClientPickItemFromEntity packet = (WrapperPlayClientPickItemFromEntity) args[2];
        Player bukkitPlayer = protocolPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) {
            return CheckResult.ALLOW;
        }
        if (shouldStripIncludeData(bukkitPlayer.getGameMode())) {
            packet.setIncludeData(false);
            return CheckResult.CHANGE;
        }
        return CheckResult.ALLOW;
    }
    @Override
    public boolean isPreVia() {
        return true;
    }
    private boolean shouldStripIncludeData(GameMode mode) {
        switch (gameModeFilter) {
            case NONE:
                return false;
            case ALL:
                return true;
            case CREATIVE:
                return mode == GameMode.CREATIVE;
            case SMART:
            default:
                return mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR;
        }
    }
}
