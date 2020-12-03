package com.fury.engine.task.executor.fast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FastScheduledExecutor {

    private static Timer fastExecutor;

    public void init() {
        fastExecutor = new Timer("Fast Executor");
    }

    //Used a cancel() won't work with a wrapper
    public Timer getExecutor() {
        return fastExecutor;
    }

    public void cancel() {
        fastExecutor.cancel();
    }

    public int purge() {
        return fastExecutor.purge();
    }

    public void schedule(TimerTask task, long delay) {
        fastExecutor.schedule(wrap(task), delay);
    }

    public void schedule(TimerTask task, Date time) {
        fastExecutor.schedule(wrap(task), time);
    }

    public void schedule(TimerTask task, long delay, long period) {
        fastExecutor.schedule(wrap(task), delay, period);
    }

    public void schedule(TimerTask task, Date firstTime, long period) {
        fastExecutor.schedule(wrap(task), firstTime, period);
    }

    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        fastExecutor.scheduleAtFixedRate(wrap(task), delay, period);
    }

    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        fastExecutor.scheduleAtFixedRate(wrap(task), firstTime, period);
    }

    public TimerTask wrap(TimerTask task) {
        return new FastExecutor(task);
    }
}
