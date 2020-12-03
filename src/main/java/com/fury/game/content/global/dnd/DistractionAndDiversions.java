package com.fury.game.content.global.dnd;

import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.global.dnd.eviltree.EvilTree;
import com.fury.game.content.global.dnd.star.ShootingStar;

import java.util.concurrent.TimeUnit;

public class DistractionAndDiversions {

    public static void init() {
        ShootingStar.init();
        GameExecutorManager.slowExecutor.schedule(EvilTree.get()::spawn, 10, TimeUnit.MINUTES);
    }
}
