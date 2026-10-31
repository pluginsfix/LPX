package com.github.retrooper.packetevents.event.simple;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import pluginsfix.lpx.api.player.ProtocolPlayer;

public class NBTDecodeEvent extends PacketEvent {
    private final ProtocolPlayer protocolPlayer;
    private final PacketEvent event;
    private NBT nbt;

    public NBTDecodeEvent(ProtocolPlayer protocolPlayer, PacketEvent event, NBT nbt) {
        this.protocolPlayer = protocolPlayer;
        this.event = event;
        this.nbt = nbt;
    }

    public ProtocolPlayer getProtocolPlayer() {
        return protocolPlayer;
    }

    public PacketEvent getEvent() {
        return event;
    }

    public NBT getNbt() {
        return nbt;
    }

    public void setNbt(NBT nbt) {
        this.nbt = nbt;
    }
}
