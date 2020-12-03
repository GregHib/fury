package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class UnlockCommand implements Command {

    private static Pattern pattern = Pattern.compile("^unlock\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "unlock ";
    }

    @Override
    public String format() {
        return "unlock [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.getMovement().unlock();
        player.message(name + " unlocked. Warning; unlocking can break activities & cause dupes/bugs.");
        PlayerLogs.log(player.getUsername(), "Unlocked player " + name + " at " + target.copyPosition() + " " + player.getControllerManager().getController());

    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
