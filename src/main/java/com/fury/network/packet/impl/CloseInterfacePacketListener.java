package com.fury.network.packet.impl;

import com.fury.game.content.dialogue.impl.misc.StartD;
import com.fury.game.network.packet.out.Interface;
import com.fury.game.world.update.flag.Flag;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class CloseInterfacePacketListener implements PacketListener {
	
	@Override
	public void handleMessage(Player player, Packet packet) {
		int interfaceId = player.getInterfaceId();
		if(player.newPlayer() && interfaceId == 30700) {
		    player.send(new Interface(interfaceId));
		    return;
        }
		if(interfaceId == 19109)
			player.getUpdateFlags().add(Flag.APPEARANCE);
		player.getPacketSender().sendClientRightClickRemoval();
		player.stopAll(true, true, true);
		if(interfaceId == 12140 && player.newPlayer())
			player.getDialogueManager().startDialogue(new StartD());
	}
}
