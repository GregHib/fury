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

public class CockatriceCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 1620 };
    }

    @Override
    public int attack(Mob mob, final Figure target) {
        MobCombatDefinitions def = mob.getCombatDefinition();
        Projectile projectile = new Projectile(mob, target, 1468, 34, 14, 35, 3, 0, 0);
        ProjectileManager.send(projectile);
        if (!Slayer.hasReflectiveEquipment(target)) {
            Player targetPlayer = (Player) target;
            Skill randomSkill = Skill.combatSkills.get(Misc.random(Skill.combatSkills.size()));
            targetPlayer.getSkills().drain(randomSkill, 0.75);
            mob.animate(7766);
            mob.graphic(1467);
            delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, targetPlayer.getMaxConstitution() / 11));
        } else
            delayHit(mob, projectile.getTickDelay(), target, getMagicHit(mob, getRandomMaxHit(mob, def.getAttackStyle(), target)));
        mob.perform(new Animation(def.getAttackAnim()));
        return mob.getCombat().getAttackSpeed();
    }
}
