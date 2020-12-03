package com.fury.game.entity.combat.scripts.godwars;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

public class KreeArraCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[] { 6222 };
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = mob.getCombatDefinition();
        if (!mob.getCombat().isInCombat()) {
            mob.animate(6997);
            delayHit(mob, 1, target, getMeleeHit(mob, getRandomMaxHit(mob, 260, MobCombatDefinitions.MELEE, target)));
            return defs.getAttackDelay();
        }
        mob.animate(6976);
        for (Figure t : mob.getPossibleTargets()) {
            if (Utils.getRandom(2) == 0)
                sendMagicAttack(mob, t);
            else {
                delayHit(mob, 1, t, getRangeHit(mob, getRandomMaxHit(mob, 720, MobCombatDefinitions.RANGE, t)));
                ProjectileManager.send(new Projectile(mob, t, 1197, 41, 16, 41, 35, 16, 0));
                Position tile;
                for(int i = 0; i < 10; i++) {
                    tile = new Position(t, 2);
                    if (World.isTileFree(tile.getX(), tile.getY(), t.getZ(), t.getSizeX(), t.getSizeY())) {
                        t.moveTo(tile);
                        break;
                    }
                }
            }
        }
        return defs.getAttackDelay();
    }

    private void sendMagicAttack(Mob mob, Figure target) {
        mob.animate(6976);
        for (Figure t : mob.getPossibleTargets()) {
            delayHit(mob, 1, t, getMagicHit(mob, getRandomMaxHit(mob, 210, MobCombatDefinitions.MAGE, t)));
            ProjectileManager.send(new Projectile(mob, t, 1198, 41, 16, 41, 35, 16, 0));
            target.perform(new Graphic(1196));
        }
    }
}
