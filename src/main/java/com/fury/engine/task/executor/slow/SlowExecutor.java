package com.fury.engine.task.executor.slow;

import com.fury.game.world.World;
import com.fury.util.Logger;

public class SlowExecutor implements Runnable {
    private Runnable runnable;

    public SlowExecutor(Runnable runnable) {
        super();
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            if (runnable != null)
                runnable.run();
        } catch (Throwable e) {
            Logger.handle(e);
            throw new RuntimeException(e);
        }
    }
}
