package com.fury.game.system.communication.commands.impl.helper;

import com.fury.game.content.controller.impl.JailController;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Jail;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class JailCommand implements Command {

    static Pattern pattern = Pattern.compile("^jail\\s(.*)\\s(\\d+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "jail ";
    }

    @Override
    public String format() {
        return "jail [username] [minutes]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);
        long minutes = Long.parseLong(values[2]);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        if(target.getVars().getLong(Variables.JAIL_TIME) > Misc.currentTimeMillis()) {
            player.message("That player is already jailed!");
            return;
        }

        if (Jail.jailPlayer(target)) {
            player.getVars().set(Variables.JAIL_TIME, Misc.currentTimeMillis() + (minutes * 60 * 1000));
            target.getControllerManager().startController(new JailController(), minutes);
            PlayerLogs.log(player.getUsername(), player.getUsername() + " just jailed " + target.getUsername() + " for " + minutes + " minutes!");
            player.message("Jailed player: " + target.getUsername() + " for " + minutes + " minutes.");
            target.save();
        }
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isStaff();
    }
}
