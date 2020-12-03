package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.files.world.increment.timer.impl.DailyVotes;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class ResetVotesCommand implements Command {

    private static Pattern pattern = Pattern.compile("^reset\\svotes\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset votes ";
    }

    @Override
    public String format() {
        return "reset votes [username]";
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

        if(DailyVotes.get().has(target.getLogger().getHardwareId())) {
            DailyVotes.get().remove(target.getLogger().getHardwareId());
            player.message(name + "'s votes have been reset.");
            PlayerLogs.log(player.getUsername(), "Reset " + name + "'s daily votes.");
        } else
            player.message("That player has no record to reset.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }

}
