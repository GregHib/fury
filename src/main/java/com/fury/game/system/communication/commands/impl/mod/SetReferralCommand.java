package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class SetReferralCommand implements Command {

    static Pattern pattern = Pattern.compile("^set\\sreferral\\s(.*)\\sreferral\\s(.*)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "set referral ";
    }

    @Override
    public String format() {
        return "set referral [name] referral [friend]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];

        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        String friend = values[2];

        target.getReferAFriend().setReferral(friend);
        player.message(target.getUsername() + "'s referral was set to '" + friend + "'.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
