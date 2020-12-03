package com.fury.game.entity.combat.scripts.rfd;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Karamel extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 3495 };
    }


    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions def = mob.getCombatDefinition();
        if (Utils.isOnRange(target, mob, 0)) {
            mob.perform(new Animation(def.getAttackAnim()));
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, 150, def.getAttackStyle(), target)));
        } else {
            if(Misc.random(3) > 0) {//MAGE
                mob.animate(1979);
                if(target.isPlayer()) {
                    Player player = (Player) target;
                    switch (Misc.random(5)) {
                        case 0:
                            mob.forceChat("Semolina-Go!");
                            target.perform(new Graphic(369));
                            target.getCombat().addFreezeDelay(20000, true);
                            break;
                        case 1:
                            player.getSkills().drain(Skill.combatSkills.get(Misc.random(Skill.combatSkills.size() - 1)), Misc.random(4) + 1);
                            target.perform(new Graphic(383));
                            break;
                        case 2:
                            player.getSettings().set(Settings.SPECIAL_ENERGY, 0);
                            CombatSpecial.updateBar(player);
                            break;
                    }
                }
                delayHit(mob, 2, target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
            } else {//RANGE
                mob.animate(2075);
                Projectile projectile = new Projectile(mob, target, 27, 44, 30, 35, 3, 10, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay(), target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
            }
        }
        return mob.getCombat().getAttackSpeed();
    }

}