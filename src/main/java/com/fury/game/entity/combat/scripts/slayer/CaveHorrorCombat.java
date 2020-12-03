package com.fury.game.entity.combat.scripts.slayer;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.slayer.Slayer;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class CaveHorrorCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Cave horror" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions def = mob.getCombatDefinition();
        if (!Slayer.hasWitchWoodIcon(target)) {
            Player targetPlayer = (Player) target;
            Skill randomSkill = Skill.combatSkills.get(Misc.random(Skill.combatSkills.size() - 1));
            targetPlayer.getSkills().drain(randomSkill, 5);
            targetPlayer.message("The screams of the cave horror make you feel slightly weaker.");
            mob.forceChat("*OOOoooAHHHH*");
            delayHit(mob, 0, target, getMeleeHit(mob, targetPlayer.getMaxConstitution() / 3));
        } else
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, def.getAttackStyle(), target)));
        mob.perform(new Animation(def.getAttackAnim()));
        return mob.getCombat().getAttackSpeed();
    }
}
