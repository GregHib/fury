package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class JadinkoCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 13820, 13821, 13822 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        boolean isDistanced = !Utils.isOnRange(mob, target, 0);
        if (target instanceof Player && ((Player) target).getPrayer().isMeleeProtecting())
            isDistanced = true;
        if (isDistanced)
            magicAttack(mob, target);
        else
            meleeAttack(mob, target);
        return mob.getCombat().getAttackSpeed();
    }

    private void magicAttack(Mob mob, Figure target) {
        mob.perform(new Animation(mob.getId() == 13820 ? 3031 : 3215));
        delayHit(mob, 2, target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
    }

    private void meleeAttack(Mob mob, Figure target) {
        mob.perform(new Animation(mob.getId() == 13820 ? 3009 : 3214));
        delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
    }
}
