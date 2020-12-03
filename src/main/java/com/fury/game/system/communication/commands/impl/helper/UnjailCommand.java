package com.fury.game.system.communication.commands.impl.helper;

import com.fury.game.content.controller.impl.JailController;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class UnjailCommand implements Command {

    static Pattern pattern = Pattern.compile("^unjail\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "unjail ";
    }

    @Override
    public String format() {
        return "unjail [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        if(target.getControllerManager().getController() instanceof JailController) {
            JailController jail = (JailController) target.getControllerManager().getController();

            jail.unjail();
            PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unjailed " + target.getUsername() + "!");
            player.message("Unjailed player: " + target.getUsername() + "");
            target.message("You have been set free by " + player.getUsername() + ".");
        } else
            player.message("That player doesn't appear to be jailed");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isStaff();
    }
}
