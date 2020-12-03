package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class GiantMoleCombat extends CombatScript {

    private static final Position[] COORDS =
            {
                    new Position(1737, 5228),
                    new Position(1751, 5233),
                    new Position(1778, 5237),
                    new Position(1736, 5227),
                    new Position(1780, 5152),
                    new Position(1758, 5162),
                    new Position(1745, 5169),
                    new Position(1760, 5183)
            };

    @Override
    public Object[] getKeys() {
        return new Object[]{3340};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        MobCombatDefinitions defs = mob.getCombatDefinition();
        if (Misc.random(5) == 0) { // bury
            mob.animate(3314);
            mob.setCantInteract(true);
            mob.getMobCombat().removeTarget();
            final Position middle = mob.getCentredPosition();
            GameWorld.schedule(2, () -> {
                mob.setCantInteract(false);
                if (mob.isDead())
                    return;
                Graphic.sendGlobal(mob, new Graphic(572), middle);
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX(), middle.getY() - 1, middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX(), middle.getY() + 1, middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX() - 1, middle.getY() - 1, middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX() - 1, middle.getY() + 1, middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX() + 1, middle.getY() - 1, middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX() + 1, middle.getY() + 1, middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX() - 1, middle.getY(), middle.getZ()));
                Graphic.sendGlobal(mob, new Graphic(571), new Position(middle.getX() + 1, middle.getY(), middle.getZ()));
                mob.moveTo(new Position(COORDS[Misc.random(COORDS.length)]));
                mob.animate(3315);
            });

        } else {
            mob.perform(new Animation(defs.getAttackAnim()));
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
        }
        return mob.getCombat().getAttackSpeed();
    }
}
