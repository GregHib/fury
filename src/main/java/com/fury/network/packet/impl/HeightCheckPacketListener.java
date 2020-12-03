package com.fury.network.packet.impl;

import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Cheap fix for the rare height exploit..
 * @author Gabriel Hannason
 */

public class HeightCheckPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int plane = packet.readByte();
		if(player.getZ() >= 0 && player.getZ() < 4) { //Only check for normal height levels, not minigames etc

			if(plane != player.getZ()) { //Player might have used a third-party-software to change their height level

				if(!player.isNeedsPlacement() && !player.getMovement().isLocked()) { //Only check if player isn't being blocked

					//Player used a third-party-software to change their height level, fix it
					player.getMovement().reset(); //Reset movement
					player.setNeedsPlacement(true); //Block upcoming movement and actions
					player.getPacketSender().sendHeight(); //Send the proper height level
					player.stopAll();
				}
			}
		}
	}
}
