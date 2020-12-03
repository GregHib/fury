package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class ResetBorkCommand implements Command {

    private static Pattern pattern = Pattern.compile("^reset\\sbork\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset bork ";
    }

    @Override
    public String format() {
        return "reset bork [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.getVars().set(Variables.LAST_BORK_DEFEAT, 0L);
        player.message(target.getUsername() + "'s bork timer was successfully reset.");
        PlayerLogs.log(player.getUsername(), "Reset " + target.getUsername() + "'s bork timer.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
