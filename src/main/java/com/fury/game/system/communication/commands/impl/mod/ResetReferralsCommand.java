package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class ResetReferralsCommand implements Command {
    private static Pattern pattern = Pattern.compile("^reset\\sreferrals\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset referrals ";
    }

    @Override
    public String format() {
        return "reset referrals [user]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        player.getReferAFriend().setFriends(new String[10]);
        player.message(target.getUsername() + "'s referred friends was successfully reset.");
        PlayerLogs.log(player.getUsername(), "Reset " + target.getUsername() + "'s referred friends.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
