package com.fury.game.entity.combat.scripts.dung;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.npc.impl.dungeoneering.RuneboundBehemoth;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.util.LinkedList;
import java.util.List;

public class RuneboundBehemothCombat extends CombatScript {
    @Override
    public Object[] getKeys() {
        return new Object[]{"Runebound behemoth"};
    }

    @Override
    public int attack(Mob mob, Figure target) {
        final RuneboundBehemoth boss = (RuneboundBehemoth) mob;
        final DungeonManager manager = boss.getManager();

        boolean trample = false;
        for (Figure t : mob.getPossibleTargets()) {
            if (Misc.colides(t.getX(), t.getY(), t.getSizeX(), t.getSizeY(), mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY())) {
                trample = true;
                delayHit(mob, 0, t, getRegularHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, t)));
                if (t.isPlayer())
                    ((Player) t).message("The beast tramples you.");
            }
        }
        if (trample) {
            mob.animate(14426);
            return 5;
        }

        if (Utils.random(15) == 0) {// Special attack
            final List<Position> explosions = new LinkedList<>();
            boss.forceChat("Raaaaaaaaaaaaaaaaaaaaaaaaaawr!");
            GameWorld.schedule(new Task(true) {
                private int cycles;
                @Override
                public void run() {
                    cycles++;
                    if (cycles == 1) {
                        boss.perform(new Graphic(2769));
                    } else if (cycles == 4) {
                        boss.perform(new Graphic(2770));
                    } else if (cycles == 5) {
                        boss.perform(new Graphic(2771));
                        for (Figure t : boss.getPossibleTargets()) {
                            for (int i = 0; i < 4; i++) {
                                Position tile = Utils.getFreeTile(t, 2);
                                if (!manager.isAtBossRoom(tile))
                                    continue;
                                explosions.add(tile);
                                ProjectileManager.send(new Projectile(boss, tile, 2414, 120, 0, 20, 0, 20, 0));
                            }
                        }
                    } else if (cycles == 8) {
                        for (Position tile : explosions)
                            Graphic.sendGlobal(boss, new Graphic(2399), tile);
                        for (Figure t : boss.getPossibleTargets()) {
                            for (Position tile : explosions) {
                                if (t.getX() != tile.getX() || t.getY() != tile.getY())
                                    continue;
                                t.getCombat().applyHit(new Hit(boss, (int) Utils.random(boss.getMaxHit() * .6, boss.getMaxHit()), HitMask.RED, CombatIcon.NONE));
                            }
                        }
                        boss.resetTransformation();
                        stop();
                    }
                }
            });
            return 8;
        }
        int[] possibleAttacks = new int[]{0, 1, 2};
        if (target instanceof Player) {
            Player player = (Player) target;
            if (player.getPrayer().isMeleeProtecting())
                possibleAttacks = new int[]{1, 2};
            else if (player.getPrayer().isRangeProtecting())
                possibleAttacks = new int[]{0, 1};
            else if (player.getPrayer().isMageProtecting())
                possibleAttacks = new int[]{0, 2};
        }
        boolean distanced = !Misc.isOnRange(mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0);
        int attack = possibleAttacks[Utils.random(possibleAttacks.length)];
        if (attack == 0 && distanced)
            attack = possibleAttacks[1];
        switch (attack) {
            case 0://melee
                boss.animate(14423);
                delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
                break;
            case 1://green exploding blob attack (magic)
                boss.animate(14427);
                //boss.setNextGraphics(new Graphics(2413));
                ProjectileManager.send(new Projectile(mob, target, 2414, 41, 16, 50, 40, 0, 0));
                delayHit(mob, 1, target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
                target.perform(new Graphic(2417, 80, 0));
                break;
            case 2://green blob attack (range)
                boss.animate(14424);
                boss.perform(new Graphic(2394));
                ProjectileManager.send(new Projectile(mob, target, 2395, 41, 16, 50, 40, 0, 2));
                delayHit(mob, 1, target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
                target.perform(new Graphic(2396, 80, 0));
                break;
        }
        return 6;
    }
}
