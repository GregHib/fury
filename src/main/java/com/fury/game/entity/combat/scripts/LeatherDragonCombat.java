package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class LeatherDragonCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{"Green dragon", "Blue dragon", "Red dragon", "Black dragon", 742, 14548, 5362, 10770, 10815, 10219};
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (Misc.isOnRange(mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0) && Misc.random(3) != 0) {
            mob.perform(new Animation(defs.getAttackAnim()));
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
        } else {// Dragonfire
            mob.animate(12259);
            mob.perform(new Graphic(1, 0, 100));
            int damage = Misc.random(600);
            damage *= CombatConstants.getDragonFireMultiplier(target, mob.getName().toLowerCase().contains("black"), false);
            delayHit(mob, 1, target, getMagicHit(mob, damage));
        }
        return mob.getCombat().getAttackSpeed();
    }
}