package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class KickCommand implements Command {

    private static Pattern pattern = Pattern.compile("^(?:(force)\\s)?kick\\s(.*)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return null;
    }

    @Override
    public String format() {
        return "(force) kick [username]";
    }

    @Override
    public void process(Player player, String... values) {
        boolean force = values[1] != null && player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
        String name = values[2];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        if (!target.isInWilderness() || force) {
            target.kick();
            player.message("Kicked " + target.getUsername() + ".");
            PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just kicked " + target.getUsername() + "!");
        } else {
            player.message("Could not kick " + target.getUsername() + " as they are in pvp.");
        }
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
