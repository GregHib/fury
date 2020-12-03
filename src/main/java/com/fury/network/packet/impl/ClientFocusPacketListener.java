package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

public class ClientFocusPacketListener implements PacketListener {
    @Override
    public void handleMessage(Player player, Packet packet) {
        //TODO implement separate counter active game time?
        //Focused != active?
//        System.out.println("Focused: " + packet.readByte());
    }
}
