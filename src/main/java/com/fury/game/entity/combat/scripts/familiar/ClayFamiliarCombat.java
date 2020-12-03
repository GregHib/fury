package com.fury.game.entity.combat.scripts.familiar;


import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;

public class ClayFamiliarCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]
		{ 8241, 8243, 8245, 8247, 8249 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		MobCombatDefinitions def = mob.getCombatDefinition();
		if (target.isPlayer()) {
			Player player = (Player) target;
			/*if (player.getAppearence().isNPC()) {
				npc.getCombat().removeTarget();
				return npc.getAttackSpeed();
			}*/
		}
		delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, mob.getCombatDefinition().getAttackStyle(), target)));
		mob.perform(new Animation(def.getAttackAnim()));
		return mob.getCombat().getAttackSpeed();
	}
}
