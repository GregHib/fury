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

public class MossTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7330, 7329 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage;
		if (usingSpecial) {// priority over regular attack
			mob.animate(8223);
			mob.perform(new Graphic(1460));
			for (Figure targets : mob.getPossibleTargets()) {
				sendSpecialAttack(targets, mob);
			}
			sendSpecialAttack(target, mob);
		} else {
			damage = getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target);
			mob.animate(8222);
			delayHit(mob, 1, target, getMeleeHit(mob, damage));
		}
		return mob.getCombat().getAttackSpeed();
	}

	public void sendSpecialAttack(Figure target, Mob mob) {
		Projectile projectile = new Projectile(mob, target, 1462, 34, 16, 35, 2, 10, 0);
		ProjectileManager.send(projectile);
		delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
		if (Misc.random(3) == 0)// 1/3 chance of being poisoned
			target.getEffects().makePoisoned(58);
	}
}
