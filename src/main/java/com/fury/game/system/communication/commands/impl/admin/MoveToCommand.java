package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class MoveToCommand implements Command {

    static Pattern pattern = Pattern.compile("^moveto\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "moveto ";
    }

    @Override
    public String format() {
        return "moveto [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Cannot find player '" + name + "' online...");
            return;
        }

        player.moveTo(target.copyPosition());
        player.message("Moved to player: " + target.getUsername() + "");
        PlayerLogs.log(player.getUsername(), "Moved to " + target.getUsername() + " " + target.copyPosition() + " " + target.getControllerManager().getController());

    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isStaff();
    }
}
