package pluginsfix.lpx.api.event;

import pluginsfix.lpx.api.check.FlagInfo;
import pluginsfix.lpx.api.check.abstraction.Check;
import pluginsfix.lpx.api.player.ProtocolPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class LPXAlertEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final ProtocolPlayer protocolPlayer;
    private final Check<?> check;
    private final FlagInfo[] infos;

    public LPXAlertEvent(ProtocolPlayer protocolPlayer, Check<?> check, FlagInfo[] infos) {
        this.protocolPlayer = protocolPlayer;
        this.check = check;
        this.infos = infos == null ? new FlagInfo[0] : infos;
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

    public FlagInfo[] getInfos() {
        return infos;
    }
}
