package com.github.retrooper.packetevents.event.simple;

import com.github.retrooper.packetevents.event.PacketEvent;
import pluginsfix.lpx.api.player.ProtocolPlayer;

public class ReadSlotsEvent extends PacketEvent {
    private final ProtocolPlayer protocolPlayer;
    private final PacketEvent event;
    private int slotsCount;

    public ReadSlotsEvent(ProtocolPlayer protocolPlayer, PacketEvent event, int slotsCount) {
        this.protocolPlayer = protocolPlayer;
        this.event = event;
        this.slotsCount = slotsCount;
    }

    public ReadSlotsEvent(PacketEvent event, int slotsCount) {
        this(null, event, slotsCount);
    }

    public ProtocolPlayer getProtocolPlayer() {
        return protocolPlayer;
    }

    public PacketEvent getEvent() {
        return event;
    }

    public int getSlotsCount() {
        return slotsCount;
    }

    public void setSlotsCount(int slotsCount) {
        this.slotsCount = slotsCount;
    }
}
