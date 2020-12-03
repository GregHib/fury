package com.fury.game.system.communication.commands.impl.helper;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class UnmuteCommand implements Command {

    private static Pattern pattern = Pattern.compile("^unmute\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "unmute ";
    }

    @Override
    public String format() {
        return "unmute [username]";
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

        Punishment.unmute(target);
        Punishment.unHardwareMute(target);

        player.message("Successfully unmuted player.");
        target.message("You have been unmuted by " + player.getUsername() + ".");
        PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just unmuted " + target.getUsername());
        PlayerLogs.log(target.getUsername(), "Unmuted by " + player.getUsername());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isStaff();
    }
}
