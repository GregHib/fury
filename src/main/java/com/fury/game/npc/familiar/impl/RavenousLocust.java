package com.fury.game.npc.familiar.impl;


import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.free.cooking.Consumables;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class RavenousLocust extends Familiar {

	public RavenousLocust(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Famine";
	}

	@Override
	public String getSpecialDescription() {
		return "Eats a peice of an opponent's food.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Figure target = (Figure) object;
		final Familiar npc = this;
		graphic(1346);
		animate(7998);
		GameWorld.schedule(1, () -> ProjectileManager.send(new Projectile(npc, target, 1347, 34, 16, 30, 35, 16, 0)));
		GameWorld.schedule(3, () -> {
			target.graphic(1348);
			if (target.isPlayer()) {
				Player playerTarget = (Player) target;
				int nextFoodSlot = Consumables.getNextFoodSlot(playerTarget);
				if (nextFoodSlot == -1)
					return;
				playerTarget.getInventory().delete(playerTarget.getInventory().get(nextFoodSlot));
			}
		});
		return true;
	}
}
