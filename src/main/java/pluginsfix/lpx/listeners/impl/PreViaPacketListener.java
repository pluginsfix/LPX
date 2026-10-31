package pluginsfix.lpx.listeners.impl;
import pluginsfix.lpx.check.dispatch.CheckDispatcher;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
public class PreViaPacketListener extends PacketListenerAbstract {
    private final CheckDispatcher dispatcher;
    public PreViaPacketListener(CheckDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        dispatcher.handlePacketReceive(event, true);
    }
    @Override
    public void onPacketEventExternal(PacketEvent event) {
        dispatcher.handleExternalEvent(event, true);
    }
    public boolean isPreVia() {
        return true;
    }
}
