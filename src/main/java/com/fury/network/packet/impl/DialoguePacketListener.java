package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

/**
 * This packet listener handles player's mouse click on the
 * "Click here to continue" option, etc.
 * 
 * @author relex lawl
 */

public class DialoguePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case PacketConstants.DIALOGUE_OPCODE:
			int id = player.getDialogueId();
			if(id != 137658 && id != 2459 && id != 2469 && id != 2480 && id != 2492)
				player.getDialogueManager().continueDialogue(-1);
			break;
		}
	}
}
