package com.fury.game.content.controller.impl;

import com.fury.game.content.controller.Controller;
import com.fury.game.world.map.instance.BossInstance;

public class BossInstanceController extends Controller {

    private BossInstance instance;

    @Override
    public void start() {
        instance = (BossInstance) getArguments()[0];
        setArguments(null);
    }

    @Override
    public boolean login() {
        if(instance != null)
            instance.leave(player, BossInstance.EXITED);
        return true;
    }

    @Override
    public boolean logout() {
        instance.leave(player, BossInstance.LOGGED_OUT);
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        instance.leave(player, BossInstance.TELEPORTED);
        removeController();
    }

    @Override
    public void forceClose() {
        instance.leave(player, BossInstance.DIED);
    }

    @Override
    public boolean sendDeath() {
        player.addStopDelay(7);
        player.stopAll();
        player.appendDeath(instance.getOutside());
        return false;
    }
}
