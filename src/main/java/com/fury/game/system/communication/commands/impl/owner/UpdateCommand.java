package com.fury.game.system.communication.commands.impl.owner;

import com.fury.Main;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.entity.character.player.PlayerBackup;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.network.packet.out.SystemUpdate;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.util.AbortBuild;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class UpdateCommand implements Command {

    static Pattern pattern = Pattern.compile("^update\\s(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "update ";
    }

    @Override
    public String format() {
        return "update (hours:)(mins:)seconds";
    }

    @Override
    public void process(Player player, String... values) {

        if (World.getUpdateTime() >= 0)
            return;

        int seconds = 0;
        if (values[1] != null)//Hours
            seconds += Integer.parseInt(values[1]) * 60 * 60;
        if (values[2] != null)//Minutes
            seconds += Integer.parseInt(values[2]) * 60;
        if (values[3] != null)//Seconds
            seconds += Integer.parseInt(values[3]);

        for (Player players : GameWorld.getPlayers()) {
            if (players == null)
                continue;
            players.send(new SystemUpdate(seconds));
        }

        //Queue login disable
        World.setUpdateTime(seconds);
        GameExecutorManager.slowExecutor.scheduleAtFixedRate(() -> {
            if (World.getUpdateTime() > 1)
                World.setUpdateTime(World.getUpdateTime() - 1);
        }, 1, 1, TimeUnit.SECONDS);

        //Queue update task
        GameExecutorManager.slowExecutor.schedule(() -> {
            Iterator<Player> iterator = GameWorld.getPlayers().iterator();
            Player user;
            while(iterator.hasNext()) {
                user = iterator.next();
                if(user != null)
                    iterator.remove();
            }
            World.saveAll(true);
            PlayerBackup.backup();
            Main.getLogger().info("Update task finished!");
            AbortBuild.attemptAbortion();
        }, seconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
