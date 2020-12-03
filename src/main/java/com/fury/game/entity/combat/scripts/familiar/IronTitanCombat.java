package com.fury.game.entity.combat.scripts.familiar;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

public class IronTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 7376, 7375 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		boolean isDistanced = !Utils.isOnRange(mob, target, 0);
		if (usingSpecial) {// priority over regular attack
			mob.animate(7954);
			mob.perform(new Graphic(1450));
			if (isDistanced)// range hit
				delayHit(mob, 2, target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)), getMagicHit(mob, getRandomMaxHit(mob, 220, MobCombatDefinitions.MAGE, target)), getMagicHit(mob, getRandomMaxHit(mob, 220, MobCombatDefinitions.MAGE, target)));
			else // melee hit
				delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)), getMeleeHit(mob, getRandomMaxHit(mob, 230, MobCombatDefinitions.MELEE, target)), getMeleeHit(mob, getRandomMaxHit(mob, 230, MobCombatDefinitions.MELEE, target)));
		} else {
			if (isDistanced) {
				mob.animate(7694);
				Projectile projectile = new Projectile(mob, target, 1452, 34, 16, 36, 2, 16, 0);
				ProjectileManager.send(projectile);
				delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, (int) (getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target) * 0.85)));
			} else {// melee
				mob.animate(7946);
				mob.perform(new Graphic(1447));
				delayHit(mob, 0, target, getMeleeHit(mob, (int) (getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target) * 0.85)));
			}
		}
		return mob.getCombat().getAttackSpeed();
	}

}
