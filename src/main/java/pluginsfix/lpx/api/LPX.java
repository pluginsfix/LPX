package pluginsfix.lpx.api;

import pluginsfix.lpx.api.adapter.InventoryViewAdapter;
import pluginsfix.lpx.api.check.abstraction.CheckManager;
import pluginsfix.lpx.api.protocol.PlayerRegistry;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

public interface LPX {
    String getServer();

    LPXPlugin getPlugin();

    PlayerRegistry getRegistry();

    CheckManager getCheckManager();

    void disable(boolean fromPluginLifecycle);

    void reloadConfig();

    default Logger getLogger() {
        return getPlugin().getLogger();
    }

    InventoryViewAdapter getInventoryViewAdapter(Player player);

    boolean isUpdated();

    void setUpdated(boolean updated);

    long getFirstJoinTime();

    void setFirstJoinTime(long firstJoinTime);

    boolean canPacketLogHeavy();
}
