package com.fury.util;

import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameSettings;

import java.util.concurrent.TimeUnit;

/**
 * Created by Greg on 16/12/2016.
 */
public class AbortBuild {

    private AbortBuild() {
        throw new IllegalStateException("Abort build");
    }

    public static void attemptAbortion() {
        if (!GameSettings.HOSTED)
            return;
        GameExecutorManager.slowExecutor.schedule(() -> System.exit(0), 15, TimeUnit.SECONDS);
    }
}
