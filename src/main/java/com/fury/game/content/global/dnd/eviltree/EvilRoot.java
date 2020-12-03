package com.fury.game.content.global.dnd.eviltree;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.global.action.Action;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EvilRoot extends GameObject {

    private EvilTree evilTree = EvilTree.get();
    private boolean spawned = false;
    private int health = Misc.random(10);
    private Optional<ScheduledFuture<?>> respawnEvent = Optional.empty();
    private Optional<ScheduledFuture<?>> stunEvent = Optional.empty();

    public EvilRoot(int id, Position position, int face) {
        super(id, position, face, Revision.RS2);
    }

    public void spawn() {
        if(evilTree.isDead())
            return;

        ObjectManager.spawnObject(this);

        EvilTreeFire fire = evilTree.getFire(this);
        if(fire != null && fire.isAlight())
            fire.despawn();

        for (Player player : GameWorld.getPlayers()) {
            if (player == null || !player.hasStarted() || player.getFinished() || !player.sameAs(this))
                continue;
            player.stopAll();
            player.message("You dive out of the way as a new root bursts from the ground.");
            player.getMovement().stepAway(this);
            player.animate(6999);
            player.perform(new Graphic(254, GraphicHeight.HIGH));
            player.getMovement().lock(5);
        }
        spawned = true;
        health = Misc.random(10);

        stunEvent = Optional.of(GameExecutorManager.slowExecutor.schedule(this::checkStun, Misc.random(1, 5), TimeUnit.SECONDS));
    }

    private void checkStun() {
        if(!spawned)
            return;
        for (Player player : GameWorld.getPlayers()) {
            if (player == null || !player.hasStarted() || player.getFinished())
                continue;

            if(player.getMovement().hasWalkSteps() && !player.isWithinDistance(this, 15) && player.isWithinDistance(this, 60)) {
                if(Misc.random(50) == 0) {
                    Graphic.sendGlobal(player, new Graphic(314), player);
                    player.forceChat("What was that?");
                    player.message("A root springs from the ground by your feet. What could have caused that?");
                }
            }

            Action action = player.getActionManager().getAction();
            if(!player.isWithinDistance(this, 1) || !(action instanceof ChopEvilTree || action instanceof LightKindling) || (action instanceof ChopEvilTree && ((ChopEvilTree) action).isRoots()))
                continue;

            if (getDirection() == 0 || getDirection() == 2) {
                if (player.getX() != getX() || player.getY() > getY() + 1 || player.getY() < getY() - 1)
                    continue;
            } else {
                if (player.getY() != getY() || player.getX() > getX() + 1 || player.getX() < getX() - 1)
                    continue;
            }

            player.stopAll();
            player.message("The evil root sweeps you out of the way.");
            player.getMovement().stepAway(this);
            player.perform(new Graphic(254, GraphicHeight.HIGH));
            player.getMovement().lock(5);
        }
        stunEvent = Optional.of(GameExecutorManager.slowExecutor.schedule(this::checkStun, Misc.random(2, 5), TimeUnit.SECONDS));
    }

    public void despawn() {
        spawned = false;
        ObjectManager.removeObject(this);
        respawnEvent.ifPresent(event -> {
            event.cancel(true);
            respawnEvent = Optional.empty();
        });
        stunEvent.ifPresent(event -> {
            event.cancel(true);
            stunEvent = Optional.empty();
        });
    }

    public boolean cut() {
        health -= 1;
        if (health <= 0) {
            despawn();
            respawnEvent = Optional.ofNullable(GameExecutorManager.slowExecutor.schedule(this::spawn, Misc.random(50, 50 + ((evilTree.getType().ordinal() + 1) * 10)), TimeUnit.SECONDS));
            return true;
        }
        return false;
    }

    public void kill(Entity source) {
        int face = getDirection();
        Graphic.sendGlobal(source, new Graphic(face == 0 ? 315 : face == 3 ? 316 : face == 2 ? 317 : 318), this);
    }
}
