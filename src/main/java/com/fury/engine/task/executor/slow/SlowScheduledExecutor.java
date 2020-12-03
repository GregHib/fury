package com.fury.engine.task.executor.slow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SlowScheduledExecutor {

    private static ScheduledExecutorService slowExecutor;

    public void init() {
        slowExecutor = Executors.newSingleThreadScheduledExecutor(new SlowThreadFactory());
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return slowExecutor.schedule(wrap(command), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return slowExecutor.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return slowExecutor.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
    }

    private Runnable wrap(Runnable command) {
        return new SlowExecutor(command);
    }

    public void execute(Runnable command) {
        slowExecutor.execute(wrap(command));
    }

    public void shutdownNow() {
        slowExecutor.shutdownNow();
    }

    public boolean isShutdown() {
        return slowExecutor.isShutdown();
    }

    public boolean isTerminated() {
        return slowExecutor.isTerminated();
    }

    public void shutdown() {
        slowExecutor.shutdown();
    }
}
