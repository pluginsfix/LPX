package pluginsfix.lpx.listeners.impl;
import pluginsfix.lpx.core.LPXCore;
import pluginsfix.lpx.network.PacketHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
public class CommandHideListener implements Listener {
    private final LPXCore core;
    public CommandHideListener(LPXCore core) {
        this.core = core;
    }
    @EventHandler
    public void commandSend(PlayerCommandSendEvent event) {
        if (PacketHandler.canSeeHiddenCommands(core, event.getPlayer(), 0L)) {
            return;
        }
        event.getCommands().removeIf(command -> command.startsWith("/"));
    }
    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}
