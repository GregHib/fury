package com.fury.engine.task.executor;

import com.fury.engine.task.executor.background.BackgroundScheduledExecutor;
import com.fury.engine.task.executor.fast.FastScheduledExecutor;
import com.fury.engine.task.executor.slow.SlowScheduledExecutor;
import com.fury.game.GameSettings;

/**
 * Created by Greg on 19/11/2016.
 */
public class GameExecutorManager {
    public static FastScheduledExecutor fastExecutor;
    public static SlowScheduledExecutor slowExecutor;
    public static BackgroundScheduledExecutor backgroundExecutor;

    public static void init() {
        long startup = System.currentTimeMillis();
        fastExecutor = new FastScheduledExecutor();
        slowExecutor = new SlowScheduledExecutor();
        backgroundExecutor = new BackgroundScheduledExecutor();
        fastExecutor.init();
        slowExecutor.init();
        backgroundExecutor.init();
        if(GameSettings.DEBUG)
            System.out.println("Loaded game executors " + (System.currentTimeMillis() - startup) + "ms");
    }

    public static void shutdown() {
        fastExecutor.cancel();
        slowExecutor.shutdownNow();
        backgroundExecutor.shutdownNow();
    }
}
