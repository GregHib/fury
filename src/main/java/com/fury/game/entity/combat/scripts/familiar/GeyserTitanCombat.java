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

public class GeyserTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7340, 7339 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		boolean isDistanced = !Utils.isOnRange(mob, target, 0);
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		if (usingSpecial) {// priority over regular attack
			mob.animate(7883);
			mob.perform(new Graphic(1373));
			Projectile projectile = new Projectile(mob, target, 1376, 34, 16, 35, 2, 10, 0);
			ProjectileManager.send(projectile);
			if (isDistanced) {// range hit
				if (Misc.random(2) == 0)
					delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
				else
					delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
			}
			else
				// melee hit
				delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
		}
		else {
			if (isDistanced) {// range
				mob.animate(7883);
				mob.perform(new Graphic(1375));
				Projectile projectile = new Projectile(mob, target, 1374, 34, 16, 35, 3, 10, 0);
				ProjectileManager.send(projectile);
				delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, (int) (getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target) * 0.85)));
			}
			else {// melee
				mob.animate(7879);
				delayHit(mob, 0, target, getMeleeHit(mob, (int) (getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target) * 0.85)));
			}
		}
		return mob.getCombat().getAttackSpeed();
	}
}
