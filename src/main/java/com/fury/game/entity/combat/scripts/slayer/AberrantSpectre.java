package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.slayer.Slayer;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class AberrantSpectre extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { "Aberrant spectre" };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions def = mob.getCombatDefinition();
        if (!Slayer.hasNosepeg(target)) {
            Player targetPlayer = (Player) target;
            if (!targetPlayer.getPrayer().isMageProtecting()) {
                Skill randomSkill = Skill.combatSkills.get(Misc.random(Skill.combatSkills.size() - 1));
                targetPlayer.getSkills().drain(randomSkill, 5);
                targetPlayer.message("The smell of the aberrant spectre make you feel slightly weaker.");
            }
            delayHit(mob, 1, target, getMagicHit(mob, targetPlayer.getMaxConstitution() / 10));
            // TODO player emote hands on ears
        } else
            delayHit(mob, 1, target, getMagicHit(mob, getRandomMaxHit(mob, mob.getCombatDefinition().getAttackStyle(), target)));
        ProjectileManager.send(new Projectile(mob, target, def.getAttackProjectile(), 18, 18, 50, 25, 0, 0));
        mob.perform(new Animation(def.getAttackAnim()));
        return mob.getCombat().getAttackSpeed();
    }
}
