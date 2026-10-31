package pluginsfix.lpx.listeners.impl;

import pluginsfix.lpx.core.LPXCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {
    private final LPXCore core;

    public LoginListener(LPXCore core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getAddress() != null) {
            return;
        }

        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(core.sZ(new Object[0]).BM(new Object[0]).VN(new Object[0]));
    }
}
