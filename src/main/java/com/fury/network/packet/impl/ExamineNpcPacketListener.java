package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.communication.commands.impl.regular.DropsCommand;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

public class ExamineNpcPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int npc = packet.readShort();
		Revision revision = Revision.values()[packet.readUnsignedByte()];
		if(npc <= 0)
			return;

		if(player.getControllerManager().getController() instanceof FirstAdventureController)
			return;

		NpcDefinition npcDef = Loader.getNpc(npc, revision);
		if(npcDef != null) {
			player.message(npcDef.getDescription());

			if (!player.getTimers().getClickDelay().elapsed(10000) && !player.getRights().isOrHigher(PlayerRights.OWNER)) {
				player.message("You can only do this once every 10 seconds.");
				return;
			}

			if(player.getSettings().getBool(Settings.EXAMINE_DROP_TABLES))
				DropsCommand.showDrops(player, npcDef);
			player.getTimers().getClickDelay().reset();
		}
	}

}
