package com.fury.game.entity.combat.scripts.glacor;

import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.npc.bosses.glacors.Glacyte;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;

public class GlacyteCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 14303 };
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        Glacyte glacyte = (Glacyte) mob;
        MobCombatDefinitions defs = mob.getCombatDefinition();
        if (target.isPlayer()) {
            Player player = (Player) target;
            if (glacyte.getEffect() == 1)
                player.getSkills().drain(Skill.PRAYER, 20);//should be only on hit
        }
        mob.perform(new Animation(defs.getAttackAnim()));
        delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, defs.getMaxHit(), target)));
        return mob.getCombat().getAttackSpeed();
    }

}