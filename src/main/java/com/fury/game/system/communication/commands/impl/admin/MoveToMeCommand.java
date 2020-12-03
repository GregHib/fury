package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class MoveToMeCommand implements Command {

    private static Pattern pattern = Pattern.compile("^movetome\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "movetome ";
    }

    @Override
    public String format() {
        return "movetome [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.getControllerManager().forceStop();
        player.message("Moving player: " + target.getUsername() + "");
        target.message("You've been moved to " + player.getUsername());
        target.moveTo(player.copyPosition());
        PlayerLogs.log(player.getUsername(), "Teleported player " + name + " to them " + player.copyPosition());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
