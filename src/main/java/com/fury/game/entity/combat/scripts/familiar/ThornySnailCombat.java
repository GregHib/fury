package com.fury.game.entity.combat.scripts.familiar;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;

public class ThornySnailCombat extends CombatScript {

	@Override
	public Object[] getKeys() { return new Object[]{ 6807, 6806 }; }

	@Override
	public int attack(Mob mob, Figure target) {
		final MobCombatDefinitions defs = mob.getCombatDefinition();
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			mob.animate(8148);
			mob.graphic(1385);
			ProjectileManager.send(new Projectile(mob, target, 1386, 34, 16, 30, 35, 16, 0));
			delayHit(mob, 1, target, getRangeHit(mob, getRandomMaxHit(mob, 80, MobCombatDefinitions.RANGE, target)));
			mob.graphic(1387);
		} else {
			mob.animate(8143);
			delayHit(mob, 1, target, getRangeHit(mob, getRandomMaxHit(mob, 40, MobCombatDefinitions.RANGE, target)));
		}
		return mob.getCombat().getAttackSpeed();
	}

}
