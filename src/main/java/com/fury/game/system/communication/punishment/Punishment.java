package com.fury.game.system.communication.punishment;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.system.files.world.single.impl.*;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

import java.util.concurrent.TimeUnit;

public class Punishment {

    /**
     * Checks
     */
    public static boolean isMuted(Player player) {
        if(Mutes.get().has(player.getUsername())) {
            long time = Mutes.get().get(player.getUsername());
            if(time > -1 && Misc.currentTimeMillis() > time) {
                Mutes.get().remove(player.getUsername());
                //TODO queue login message "your mute is up"
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isHardwareMuted(Player player) {
        if(HWID.Mutes.get().has(player.getUsername())) {
            long time = HWID.Mutes.get().get(player.getUsername());
            if(time > -1 && Misc.currentTimeMillis() > time) {
                HWID.Mutes.get().remove(player.getUsername());
                //TODO queue login message "your mute is up"
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isBanned(String username) {
        if(Bans.get().has(username)) {
            long time = Bans.get().get(username);
            if(time > -1 && Misc.currentTimeMillis() > time) {
                Bans.get().remove(username);
                //TODO queue login message "your ban has been removed, don't break the rules again"
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isHardwareBanned(String hwid) {
        if(HWID.Bans.get().has(hwid)) {
            long time = HWID.Bans.get().get(hwid);
            if(time > -1 && Misc.currentTimeMillis() > time) {
                HWID.Bans.get().remove(hwid);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Revokes
     */

    public static void unmute(Player player) {
        Mutes.get().remove(player.getUsername());
    }

    public static void unHardwareMute(Player player) {
        HWID.Mutes.get().remove(player.getLogger().getHardwareId());
    }

    public static void unban(Player player) {
        Bans.get().remove(player.getUsername());
    }

    public static void unHardwareBan(Player player) {
        HWID.Bans.get().remove(player.getLogger().getHardwareId());
    }

    public static void unblock(Player player) {
        unban(player);
        unHardwareBan(player);
    }


    /**
     * Punishments
     */

    public static void mute(Player player) {
        mute(player, -1);
    }

    public static void mute(Player player, int minutes) {
        Mutes.get().record(player.getUsername(), convert(minutes));
    }

    public static void hardwareMute(Player player) {
        hardwareMute(player, -1);
    }

    public static void hardwareMute(Player player, int minutes) {
        HWID.Mutes.get().record(player.getLogger().getHardwareId(), convert(minutes));
    }

    public static void ban(Player player) {
        ban(player, -1);
    }

    public static void ban(Player player, int minutes) {
        Bans.get().record(player.getUsername(), convert(minutes));
    }

    public static void hardwareBan(Player player) {
        hardwareBan(player, -1);
    }

    public static void hardwareBan(Player player, int minutes) {
        HWID.Bans.get().record(player.getLogger().getHardwareId(), convert(minutes));
    }

    public static void block(Player player) {
        ban(player);
        hardwareBan(player);
    }

    /**
     * Misc
     */

    private static long convert(int minutes) {
        return minutes < 0 ? -1 : Misc.currentTimeMillis() + (minutes * 60000);
    }

    public static void kickWithAnimation(Player target) {
        Graphic graphic = new Graphic(3396, Revision.PRE_RS3);
        Animation animation = new Animation(17523, Revision.PRE_RS3);
        switch (Misc.random(3)) {
            case 1:
                animation = new Animation(17544, Revision.PRE_RS3);
                graphic = new Graphic(3403, Revision.PRE_RS3);
                break;
            case 2:
                animation = new Animation(17542, Revision.PRE_RS3);
                graphic = new Graphic(3402, Revision.PRE_RS3);
                break;
            case 3:
                animation = new Animation(17532, Revision.PRE_RS3);
                graphic = new Graphic(3397, Revision.PRE_RS3);
                break;
        }

        target.perform(graphic);
        target.perform(animation);

        GameExecutorManager.slowExecutor.schedule(() -> {
            for (Player p : GameWorld.getPlayers()) {
                if (p == null)
                    continue;
                if (p.getLogger().getHardwareId().equals(target.getLogger().getHardwareId()))
                    p.kick();
            }
        }, Loader.getAnimation(animation.getId(), animation.getRevision()).getEmoteTime(), TimeUnit.MILLISECONDS);
    }
}
