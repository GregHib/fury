package com.fury.game.entity.combat.scripts.familiar;

import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;

public class AbbysalTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 7350, 7349 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		int damage = 0;
		damage = getRandomMaxHit(mob, mob.getCombatDefinition().getMaxHit(), MobCombatDefinitions.MELEE, target);
		mob.animate(7980);
		mob.graphic(1490);

		if (target.isPlayer()) { // cjay failed dragonkk saved the day
			Player player = (Player) target;
			if (damage > 0)
				player.getSkills().drain(Skill.PRAYER, damage / 2);
		}
		delayHit(mob, 0, target, getMeleeHit(mob, damage));
		return mob.getCombat().getAttackSpeed();
	}
}
