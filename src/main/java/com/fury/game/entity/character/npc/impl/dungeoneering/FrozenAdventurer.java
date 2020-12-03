package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.map.Position;
import com.fury.core.model.node.entity.actor.figure.player.Player;

@SuppressWarnings("serial")
public class FrozenAdventurer extends Mob {

	private Player player;

	public FrozenAdventurer(int id, Position tile) {
		super(id, tile);
	}

	@Override
	public void processNpc() {
		if (player == null || player.isDying() || player.getFinished()) {
			deregister();
			return;
		}/* else if (!player.getTransformationId().isNPC()) {
			ToKashBloodChillerCombat.removeSpecialFreeze(player);
			deregister();
			return;
		}*/
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getFrozenPlayer() {
		return player;
	}

}
