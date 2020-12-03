package com.fury.game.entity.combat.scripts.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;

public class CatableponCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 4397, 4398, 4399 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();

        if(target.isWithinDistance(mob, 1)) {
            delayHit(mob, 1, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
        } else {
            Hit hit = getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target));
            if(target.isPlayer() && hit.getDamage() > 0) {
                Player player = (Player) target;
                player.getSkills().drain(Skill.STRENGTH, 0.05);
                player.message("You feel slightly weakened.", true);
            }
            Projectile projectile = new Projectile(mob, target, 106, 44, 3, 43, 31, 0, 0);
            ProjectileManager.send(projectile);
            delayHit(mob, projectile.getTickDelay(), target, hit);
        }
        return defs.getAttackDelay();
    }
}
