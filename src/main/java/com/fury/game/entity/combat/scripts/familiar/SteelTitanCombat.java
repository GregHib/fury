package com.fury.game.entity.combat.scripts.familiar;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class SteelTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7344, 7343 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		boolean isDistanced = !Utils.isOnRange(mob, target, 0);
		if (usingSpecial) {// priority over regular attack
			mob.animate(8190);
			target.perform(new Graphic(1449));
			if (isDistanced) {// range hit
				delayHit(mob, 2, target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)), getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)), getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)), getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
			} else {// melee hit
				delayHit(mob, 1, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)), getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)), getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)), getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
			}
		}
		else {
			int style = Misc.random(isDistanced ? 1 : 0, 3);
			switch (style) {
				case 0://MELEE
					mob.animate(8183);
					delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
					break;
				case 1://RANGE
					mob.animate(8190);
					Projectile projectile = new Projectile(mob, target, 1445, 34, 16, 35, 2, 10, 0);
					ProjectileManager.send(projectile);
					delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
					break;
				case 2://MAGE
					mob.animate(7694);
					projectile = new Projectile(mob, target, 1451, 34, 16, 35, 2, 10, 0);
					ProjectileManager.send(projectile);
					delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
					break;
			}
		}
		return mob.getCombat().getAttackSpeed();
	}
}
