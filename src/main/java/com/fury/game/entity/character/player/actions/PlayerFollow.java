package com.fury.game.entity.character.player.actions;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.global.action.Action;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.map.path.EntityStrategy;
import com.fury.game.world.map.path.PathFinder;
import com.fury.util.Misc;

import java.util.LinkedList;

public class PlayerFollow extends Action {
    private Player target;

    public PlayerFollow(Player target) {
        this.target = target;
    }

    @Override
    public boolean start(Player player) {
        player.getDirection().face(target);
        if (checkAll(player))
            return true;
        player.getDirection().face(null);
        return false;
    }

    private boolean checkAll(Player player) {
        try {
            if (player.isDead() || player.getFinished() || target.isDead() || target.getFinished())
                return false;
            if (player.getZ() != target.getZ())
                return false;
            if(player.getControllerManager().getController() instanceof FirstAdventureController)
                return false;
            if (/*player.isBound() ||*/ player.isStunned())
                return true;
            int distanceX = player.getX() - target.getX();
            int distanceY = player.getY() - target.getY();
            int sizeX = player.getSizeX();
            int sizeY = player.getSizeY();
            int maxDistance = 16;
            if (player.getZ() != target.getZ() || distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance)
                return false;

            int lastFaceEntity = target.getDirection().getInteracting() != null && target.getDirection().getInteracting() instanceof Player ? ((Player) target.getDirection().getInteracting()).getIndex() : -1;
            if (lastFaceEntity == player.getIndex() && target.getActionManager().getAction() instanceof PlayerFollow)
                player.getMovement().addWalkSteps(target.getMovement().getLastPosition().getX(), target.getMovement().getLastPosition().getY());
            else if (!player.getCombat().clippedProjectile(target, true) || !Misc.isOnRange(player.getX(), player.getY(), sizeX, sizeY, target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0)) {
                int steps = PathFinder.findRoute(player, player.getSizeX(), player.getSizeY(), new EntityStrategy(target), true);

                if (steps == -1)
                    return false;

                if (steps > 0) {
                    player.getMovement().reset();

                    LinkedList<Integer> bufferX = PathFinder.getLastPathBufferX();
                    LinkedList<Integer> bufferY = PathFinder.getLastPathBufferY();
                    for (int step = steps - 1; step >= 0; step--)
                        if (!player.getMovement().addWalkSteps(bufferX.get(step), bufferY.get(step), 25, true))
                            break;
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        return 0;
    }

    @Override
    public void stop(final Player player) {
        player.getDirection().setInteracting(null);
    }
}
