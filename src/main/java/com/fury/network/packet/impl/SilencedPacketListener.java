package com.fury.network.packet.impl;

import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * This packet listener is called when a packet should not execute
 * any particular action or event, but will also not print out
 * any debug information.
 * 
 * @author relex lawl
 */

public class SilencedPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
	}

}
