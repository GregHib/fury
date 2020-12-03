package com.fury.game.system.communication.commands.impl.helper;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.communication.punishment.Punishment;
import com.fury.game.world.World;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class MuteCommand implements Command {

    private static Pattern pattern = Pattern.compile("^mute\\s(.*)\\s(-?\\d+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "mute ";
    }

    @Override
    public String format() {
        return "mute [username] [minutes (-1 for permanent)]";
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

        target.perform(new Graphic(3401, Revision.PRE_RS3));

        Punishment.mute(target, minutes);
        Punishment.hardwareMute(target, minutes);

        player.message("Successfully muted player.");
        target.message("You have been muted by " + player.getUsername() + ".");
        PlayerLogs.log(player.getUsername(), "" + player.getUsername() + " just muted " + target.getUsername());
        PlayerLogs.log(target.getUsername(), "Muted for " + minutes + " by " + player.getUsername());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isStaff();
    }
}
