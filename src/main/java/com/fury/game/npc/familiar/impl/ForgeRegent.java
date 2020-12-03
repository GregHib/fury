package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class ForgeRegent extends Familiar {

	public ForgeRegent(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Inferno";
	}

	@Override
	public String getSpecialDescription() {
		return "A magical attack that disarms an enemy's weapon or shield.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Figure target = (Figure) object;
		getOwner().graphic(1316);
		getOwner().animate(7660);
		animate(7871);
		perform(new Graphic(1394));
		GameWorld.schedule(2, () -> {
			if (target.isPlayer()) {
				Player playerTarget = (Player) target;
//					int weaponId = playerTarget.getEquipment().get(Slot.WEAPON).getId();
//					if (weaponId != -1) {
//						//if (PlayerCombatNew. != 423) {
//							ButtonHandler.sendRemove(playerTarget, 3, false);
//						//}
//					}
//					int shieldId = playerTarget.getEquipment().getShieldId();
//					if (shieldId != -1) {
//						ButtonHandler.sendRemove(playerTarget, 5, false);
//					}
			}
			target.perform(new Graphic(1393));
			target.getCombat().applyHit(new Hit(getOwner(), Misc.random(200), HitMask.RED, CombatIcon.MELEE));
		});
		return true;
	}
}
