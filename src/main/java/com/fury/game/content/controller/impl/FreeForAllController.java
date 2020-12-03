package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.Controller;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.PlayerDeath;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Colours;
import com.fury.util.FontUtils;

public class FreeForAllController extends Controller {

    private boolean wasInArea;

    @Override
    public void start() {
        player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " Welcome to the free-for-all arena! You will not lose any items on death here.", Colours.BLUE);
        sendInterfaces();
    }

    private boolean isRisk() {
        if (getArguments() == null || getArguments().length < 1)
            return false;
        return (Boolean) getArguments()[0];
    }

    @Override
    public void sendInterfaces() {
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(6);
        player.stopAll();
        GameWorld.schedule(new PlayerDeath(player, player.copyPosition(), new Position(2993, 9679), false, new Runnable() {
            @Override
            public void run() {
                if (isRisk()) {
                    Player killer = player.getCombat().getHits().getMostDamageReceivedSourcePlayer();
                    if (killer != null) {
//                            killer.reduceDamage(player);
//                            player.sendItemsOnDeath(killer, true);
                    }
                }
            }
        }));
        return false;
    }

    @Override
    public void magicTeleported(int type) {
        remove(true);
    }


    @Override
    public void forceClose() {
        remove(false);
    }

    private void remove(boolean needRemove) {
        if (needRemove)
            removeController();
        if (wasInArea)
            player.setCanPvp(false);
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        switch (object.getId()) {
            case 38700:
                remove(true);
                ObjectHandler.useStairs(player, -1, new Position(2993, 9679), 0, 1);
                return false;
        }
        return true;
    }

    @Override
    public void moved() {
        boolean inArea = inPvpArea(player);
        if (inArea && !wasInArea) {
            player.setCanPvp(true);
            wasInArea = true;
        } else if (!inArea && wasInArea) {
            player.setCanPvp(false);
            wasInArea = false;
        }
    }

    @Override
    public boolean canAttack(Figure target) {
        if (canHit(target))
            return true;
        return false;
    }

    @Override
    public boolean canHit(Figure target) {
        return true;
    }

    private boolean inPvpArea(Player player) {
        return player.getY() >= 5512;
    }

    @Override
    public boolean logout() {
        return false; //else removes controller
    }

    @Override
    public boolean login() {
        return false;
    }

    public static boolean isOverloadChanged(Player player) {
        if (!(player.getControllerManager().getController() instanceof FreeForAllController)) {
            return false;
        }
        return player.isCanPvp() && ((FreeForAllController) player.getControllerManager().getController()).isRisk();
    }
}
