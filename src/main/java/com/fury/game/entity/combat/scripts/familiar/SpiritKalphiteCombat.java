package com.fury.game.entity.combat.scripts.familiar;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class SpiritKalphiteCombat extends CombatScript {

	@Override
	public Object[] getKeys() { return new Object[]{ 6995, 6994 }; }

	@Override
	public int attack(Mob mob, Figure target) {
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage;
		if (usingSpecial) {// TODO find special
			mob.animate(8519);
			mob.perform(new Graphic(8519));
			damage = getRandomMaxHit(mob, (int) (MobCombatDefinitions.MELEE * 0.65), target);
			delayHit(mob, 1, target, getMeleeHit(mob, damage));
		} else {
			mob.animate(8519);
			damage = getRandomMaxHit(mob, 50, MobCombatDefinitions.MELEE, target);
			delayHit(mob, 1, target, getMeleeHit(mob, damage));
		}
		return mob.getCombat().getAttackSpeed();
	}
}
