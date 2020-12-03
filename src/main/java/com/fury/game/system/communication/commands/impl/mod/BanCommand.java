package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class BanCommand implements Command {

    private static Pattern pattern = Pattern.compile("^ban\\s(.*)\\s(-?\\d+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "ban ";
    }

    @Override
    public String format() {
        return "ban [username] [minutes (-1 for permanent)";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);
        int minutes = (int) Long.parseLong(values[2]);

        if(target == null) {
            player.message("'" + name + "' is not currently online.");
            target = SearchUtils.getPlayerFromName(name);
        }

        if (target == null) {
            player.message("Error loading players file.");
            return;
        }


        Punishment.ban(target, minutes);
        Punishment.hardwareBan(target, minutes);

        Punishment.kickWithAnimation(target);

        player.message("Successfully banned player.");
        PlayerLogs.log(player.getUsername(), "Banned " + target.getUsername());
        PlayerLogs.log(target.getUsername(), "Banned for " + minutes + " by " + player.getUsername());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
