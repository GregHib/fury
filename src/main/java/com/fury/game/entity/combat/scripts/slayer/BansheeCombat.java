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

public class BansheeCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { "Banshee", "Mighty banshee" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions def = mob.getCombatDefinition();
        if (!Slayer.hasEarmuffs(target)) {
            Player targetPlayer = (Player) target;
            if (!targetPlayer.getPrayer().isMeleeProtecting()) {
                Skill randomSkill = Skill.combatSkills.get(Misc.random(Skill.combatSkills.size() - 1));
                targetPlayer.getSkills().drain(randomSkill, 5);
                targetPlayer.message("The screams of the banshee make you feel slightly weaker.");
                mob.forceChat("*EEEEHHHAHHH*");
            }
            delayHit(mob, 0, target, getMeleeHit(mob, 80));
            //TODO player emote hands on ears
        } else
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, def.getAttackStyle(), target)));
        mob.perform(new Animation(def.getAttackAnim()));
        return mob.getCombat().getAttackSpeed();
    }
}