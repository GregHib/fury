package com.fury.game.entity.combat.scripts;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitionsLoader;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KalphiteQueenCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[]{ 1158, 1160 };
    }

    private static void attackMageTarget(final List<Player> arrayList, Figure fromFigure, final Mob mob, Figure t) {
        final Figure target = t == null ? getTarget(arrayList, fromFigure, mob) : t;
        if (target == null)
            return;
        if (target.isPlayer())
            arrayList.add((Player) target);
        Projectile projectile = fromFigure == mob ? new Projectile(mob, target, 280, mob.getId() == 1158 ? 58 : 68, 30, 10, 53, 20, 50) : new Projectile(fromFigure, target, 5048, 30, 30, 30, 3, 0, 0);
        ProjectileManager.send(projectile);
        GameWorld.schedule(projectile.getTickDelay(), () -> {
            int damage = Misc.random(mob.getCombatDefinition().getMaxHit());
//				if (target.isPlayer() && ((Player) target).getPrayer().isMageProtecting())
//					damage /= 2;

            delayHit(mob, 0, target, getMagicHit(mob, damage));
            target.perform(new Graphic(281, 0, 0));
            attackMageTarget(arrayList, target, mob, null);
        });
    }

    public static Player getTarget(List<Player> list, final Figure fromFigure, Mob startTile) {
        if (fromFigure == null)
            return null;
        ArrayList<Player> added = new ArrayList<>();
        for (Figure figure : startTile.getPossibleTargets()) {
            if (!figure.isPlayer())
                continue;
            Player player = (Player) figure;
            if (player == null || list.contains(player) || !player.isViewableFrom(fromFigure) || !player.isViewableFrom(startTile))
                continue;
            added.add(player);
        }
        if (added.isEmpty())
            return null;
        Collections.sort(added, (o1, o2) -> {
            if (o1 == null)
                return 1;
            if (o2 == null)
                return -1;
            if (Misc.getDistance(o1, fromFigure) > Misc.getDistance(o2, fromFigure))
                return 1;
            else if (Misc.getDistance(o1, fromFigure) < Misc.getDistance(o2, fromFigure))
                return -1;
            else
                return 0;
        });
        return added.get(0);

    }

    private static final Graphic FIRST_MAGIC_START = new Graphic(278), SECOND_MAGIC_START = new Graphic(279);

    @Override
    public int attack(Mob mob, Figure target) {
        final MobCombatDefinitions defs = MobCombatDefinitionsLoader.forId(mob.getId(), mob.getRevision());
        boolean secondForm = mob.getId() != 1158;
        int style = Misc.random(!Misc.isOnRange(mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0) ? 1 : 0, 3);
        switch(style) {
            case 0://Melee
                mob.perform(new Animation(secondForm ? 6235 : 6241));
                int damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.MELEE, target);
                delayHit(mob, 0, target, getMeleeHit(mob, damage));
                break;
            case 1://Magic
                mob.perform(new Animation(secondForm ? 6234 : 6240));
                mob.perform(secondForm ? SECOND_MAGIC_START : FIRST_MAGIC_START);
                attackMageTarget(new ArrayList<>(), mob, mob, target);
                break;
            case 2://Range
                mob.perform(new Animation(secondForm ? 6234 : 6240));
                ProjectileManager.send(new Projectile(mob, target, secondForm ? 473 : 473, secondForm ? 30 : 58, 30, 20, 25, 10, 0));
                for (Figure t : mob.getPossibleTargets()) {
//                    t.perform(new Graphic(secondForm ? 5045 : 5043, projectile.getEndTime(), 100));
                    damage = getRandomMaxHit(mob, defs.getMaxHit(), MobCombatDefinitions.RANGE, target);
//					if (t.isPlayer() && ((Player) t).getPrayer().isRangeProtecting())
//						damage /= 2;
                    delayHit(mob, 0, t, getRangeHit(mob, damage));
                }
                break;
        }
        return defs.getAttackDelay();
    }
}
