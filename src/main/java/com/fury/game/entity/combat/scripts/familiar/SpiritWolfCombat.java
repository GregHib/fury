package com.fury.game.entity.combat.scripts.familiar;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class SpiritWolfCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 6829, 6828 };
	}

	@Override
	public int attack(final Mob mob, final Figure target) {
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			familiar.submitSpecial(familiar.getOwner());
			mob.animate(8293);
			mob.perform(new Graphic(1334));
			ProjectileManager.send(new Projectile(mob, target, 1333, 34, 16, 35, 2, 10, 0));
			if (target.isNpc()) {
				if (target.getSize() < 2)
					target.getCombat().setAttackedByDelay(3000);// three seconds
				else
					familiar.getOwner().message("Your familiar cannot scare that monster.");
			} else if (target.isPlayer())
				familiar.getOwner().message("Your familiar cannot scare a player.");
			else if (target.isFamiliar())
				familiar.getOwner().message("Your familiar cannot scare other familiars.");
		}
		else {
			mob.animate(6829);
			delayHit(mob, 1, target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
		}
		return mob.getCombat().getAttackSpeed();
	}

}
