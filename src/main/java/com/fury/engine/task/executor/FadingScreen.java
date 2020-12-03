package com.fury.engine.task.executor;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.util.Utils;

import java.util.TimerTask;


public final class FadingScreen {

    private FadingScreen() {

    }

    public static void fade(final Player player, long fadeTime, final Runnable event) {
        unfade(player, fade(player, fadeTime), event);
    }

    public static void fade(final Player player, final Runnable event) {
        unfade(player, fade(player), event);
    }

    public static void unfade(final Player player, long startTime, final Runnable event) {
        unfade(player, 2500, startTime, event);
    }

    public static void unfade(final Player player, long endTime, long startTime, final Runnable event) {
        long leftTime = endTime - (Utils.currentTimeMillis() - startTime);
        if (leftTime > 0) {
            GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                @Override
                public void run() {
                    unfade(player, event);
                }
            }, leftTime);
        } else
            unfade(player, event);
    }

    public static void unfade(final Player player, Runnable event) {
        event.run();
        /*WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                //player.getInterfaceManager().sendFadingInterface(170);
                GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //player.getInterfaceManager().removeFadingInterface();
                    }
                }, 2000);
            }

        });*/
    }

    public static long fade(Player player, long fadeTime) {
        //player.getInterfaceManager().sendFadingInterface(115);
        return Utils.currentTimeMillis() + fadeTime;
    }

    public static long fade(Player player) {
        return fade(player, 0);
    }
}

