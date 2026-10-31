package pluginsfix.lpx.api.event;

import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class LPXPunishEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final ProtocolPlayer protocolPlayer;
    private final Check<?> check;

    public LPXPunishEvent(ProtocolPlayer protocolPlayer, Check<?> check) {
        this.protocolPlayer = protocolPlayer;
        this.check = check;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ProtocolPlayer getProtocolPlayer() {
        return protocolPlayer;
    }

    public Check<?> getCheck() {
        return check;
    }
}
