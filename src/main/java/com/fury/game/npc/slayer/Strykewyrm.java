package com.fury.game.npc.slayer;

import com.fury.cache.def.Loader;
import com.fury.core.task.TickableTask;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.slayer.SlayerManager;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class Strykewyrm extends Mob {

    private int nonCombatTicks;

    public Strykewyrm(int id, Position tile) {
        super(id, tile, false);
        setCantFollowUnderCombat(true);
        nonCombatTicks = 1;
    }

    @Override
    public void processNpc() {
        super.processNpc();
        if (isDead() || getId() % 2 == 0 || isCantInteract())
            return;
        if (!getCombat().isInCombat())
            nonCombatTicks++;
        if (nonCombatTicks % 17 == 0) {
            animate(12796);
            GameWorld.schedule(1, (Runnable) this::reset);
        }
    }

    @Override
    public void reset() {
        setTransformation(getId() - 1);
        setCantInteract(false);
        nonCombatTicks = 1;
        super.reset();
    }

    public static void handleStomping(final Player player, final Mob mob) {
        if (mob.isCantInteract())
            return;
        int requiredLevel = mob.getId() == 9462 ? 93 : mob.getId() == 9464 ? 77 : mob.getId() == 9466 ? 73 : 1;
        SlayerManager manager = player.getSlayerManager();
        if (requiredLevel != 1 && !manager.isCurrentTask(Loader.getNpc(mob.getId() + 1, mob.getRevision()).getName())) {
            player.message("You need to have strykewyrm assigned as a task in order to fight them.");
            return;
        }

        if (player.getSkills().getLevel(Skill.SLAYER) < requiredLevel) {
            player.message("You need a Slayer level of at least " + requiredLevel + " to fight a Stykewyrm.");
            return;
        }
        player.animate(4278);
        mob.setCantInteract(true);
        GameWorld.schedule(new TickableTask(true) {
            @Override
            public void tick() {
                if (getTick() == 1) {
                    mob.animate(12795);
                    mob.setTransformation(mob.getId() + 1);
                    mob.getHealth().setHitpoints(mob.getMaxConstitution());
                } else if (getTick() == 3) {
                    mob.getMobCombat().setTarget(player);
                    mob.setCantInteract(false);
                    stop();
                }
            }
        });
    }
}