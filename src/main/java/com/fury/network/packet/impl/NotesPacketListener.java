package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;


/**
 * Note saving and changes
 * @author Greg
 */

public class NotesPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int code = packet.getOpcode();
		switch (code) {
		case PacketConstants.NOTE_TEXT_OPCODE:
			saveNote(player, packet);
			break;
		case PacketConstants.NOTE_COMMAND_OPCODE:
			handleNotes(player, packet);
			break;
		}
	}
	
	private void handleNotes(Player player, Packet packet) {
		int command = packet.readShort();
		int noteId = packet.readShort();
		switch(command) {
		case 0://Save
			player.getNotes().add(noteId, (String) player.getTemporaryAttributes().getOrDefault("tempNote", ""));
            player.getTemporaryAttributes().put("tempNote", "");
			break;
		case 1://Delete
			player.getNotes().delete(noteId);
			break;
		case 2:
		case 3:
		case 4:
		case 5://Colour
			player.getNotes().setColour(noteId, command-1);
			break;
		case 6://Delete all
            player.getNotes().deleteAll();
			break;
		}
	}
	
	private void saveNote(Player player, Packet packet) {
		String message = packet.readString();
		if(player.getTemporaryAttributes().getOrDefault("tempNote", "").equals(""))
			player.getTemporaryAttributes().put("tempNote", message);
	}
}
