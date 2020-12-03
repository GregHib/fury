package com.fury.engine.task.executor.fast;

import com.fury.game.world.World;
import com.fury.util.Logger;

import java.util.TimerTask;

public class FastExecutor extends TimerTask {

    private TimerTask task;

    public FastExecutor(TimerTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            if (task != null)
                task.run();
        } catch (Throwable e) {
            Logger.handle(e);
        }
    }
}
