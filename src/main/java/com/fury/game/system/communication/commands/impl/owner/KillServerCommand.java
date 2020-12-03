package com.fury.game.system.communication.commands.impl.owner;

import com.fury.Main;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.PlayerBackup;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.network.packet.out.SystemUpdate;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.util.AbortBuild;

import java.util.TimerTask;
import java.util.regex.Pattern;

public class KillServerCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "kill server";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {


        for (Player p : GameWorld.getPlayers()) {
            if (p == null)
                continue;
            p.send(new SystemUpdate(0));
        }

        //Queue login disable
        World.setUpdateTime(0);


        for (Player user : GameWorld.getPlayers())
            if (user != null)
                GameWorld.getPlayers().remove(user);

        World.saveAll(true);
        PlayerBackup.backup();
        Main.getLogger().info("Update task finished!");
        AbortBuild.attemptAbortion();
        GameExecutorManager.fastExecutor.schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 15000);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
