package com.github.retrooper.packetevents.event.simple;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import pluginsfix.lpx.api.player.ProtocolPlayer;

public class ItemStackDecodeEvent extends PacketEvent {
    private final ProtocolPlayer protocolPlayer;
    private final PacketEvent event;
    private ItemStack itemStack;

    public ItemStackDecodeEvent(ProtocolPlayer protocolPlayer, PacketEvent event, ItemStack itemStack) {
        this.protocolPlayer = protocolPlayer;
        this.event = event;
        this.itemStack = itemStack;
    }

    public ProtocolPlayer getProtocolPlayer() {
        return protocolPlayer;
    }

    public PacketEvent getEvent() {
        return event;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
