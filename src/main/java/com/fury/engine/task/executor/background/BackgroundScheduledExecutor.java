package com.fury.engine.task.executor.background;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BackgroundScheduledExecutor {

    private static ScheduledExecutorService backgroundExecutor;

    public void init() {
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor(new BackgroundThreadFactory());
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return backgroundExecutor.schedule(wrap(command), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return backgroundExecutor.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return backgroundExecutor.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
    }

    private Runnable wrap(Runnable command) {
        return new BackgroundExecutor(command);
    }

    public void execute(Runnable command) {
        backgroundExecutor.execute(wrap(command));
    }

    public void shutdownNow() {
        backgroundExecutor.shutdownNow();
    }

    public boolean isShutdown() {
        return backgroundExecutor.isShutdown();
    }

    public boolean isTerminated() {
        return backgroundExecutor.isTerminated();
    }

    public void shutdown() {
        backgroundExecutor.shutdown();
    }
}
