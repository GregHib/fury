package com.fury.game.entity.combat.scripts.familiar;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class LavaTitanCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { 7342, 7341 };
	}

	@Override
	public int attack(Mob mob, Figure target) {
		Familiar familiar = (Familiar) mob;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (usingSpecial) {// priority over regular attack
			mob.animate(7883);
			mob.perform(new Graphic(1491));
			delayHit(mob, 1, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
			if (damage <= 4 && target.isPlayer()) {
				Player player = (Player) target;
				player.getSettings().set(Settings.SPECIAL_ENERGY, player.getSettings().getInt(Settings.SPECIAL_ENERGY) / 10);
				CombatSpecial.updateBar(player);
			}
		}
		else {
			damage = (int) (getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target) * 0.85);
			mob.animate(7980);
			mob.perform(new Graphic(1490));
			delayHit(mob, 1, target, getMeleeHit(mob, damage));
		}
		if (Misc.random(10) == 0)// 1/10 chance of happening
			delayHit(mob, 1, target, getMeleeHit(mob, Misc.random(500)));
		return mob.getCombat().getAttackSpeed();
	}
}
