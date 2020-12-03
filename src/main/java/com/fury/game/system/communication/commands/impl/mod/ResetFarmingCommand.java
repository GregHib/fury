package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class ResetFarmingCommand implements Command {

    private static Pattern pattern = Pattern.compile("^reset\\sfarming\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset farming ";
    }

    @Override
    public String format() {
        return "reset farming [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.getFarmingManager().resetSpots();
        target.getFarmingManager().resetTreeTrunks();
        player.message(target.getUsername() + "'s farming spots was successfully reset.");
        target.message(player.getUsername() + " has reset your farming patches, please relog to see the changes.");
        PlayerLogs.log(player.getUsername(), "Reset " + target.getUsername() + "'s farming spots.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
