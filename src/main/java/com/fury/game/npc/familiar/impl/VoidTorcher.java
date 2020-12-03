package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class VoidTorcher extends Familiar {

	public VoidTorcher(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Call To Arms";
	}

	@Override
	public String getSpecialDescription() {
		return "Teleports the player to Void Outpost.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 4;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		TeleportHandler.teleportPlayer((Player) object, new Position(2662, 2649), TeleportType.NORMAL);
//		Magic.sendTeleportSpell((Player) object, 14388, -1, 1503, 1502, 0, 0, new Position(2662, 2649), 3, true, Magic.OBJECT_TELEPORT);
		return true;
	}
}
