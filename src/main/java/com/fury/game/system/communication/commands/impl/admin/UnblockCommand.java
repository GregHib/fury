package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class UnblockCommand implements Command {

    private static Pattern pattern = Pattern.compile("^unblock\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "unblock ";
    }

    @Override
    public String format() {
        return "unblock [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if(target == null) {
            player.message("'" + name + "' is not currently online.");
            target = SearchUtils.getPlayerFromName(name);
        }

        if (target == null) {
            player.message("Error loading players file.");
            return;
        }

        Punishment.unblock(target);
        player.message("Successfully unblocked player.");
        PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unblocked " + target.getUsername());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
