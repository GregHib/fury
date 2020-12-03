package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class VoteCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "vote";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getPacketSender().sendUrl(GameSettings.WEBSITE + "/vote/vote.php?username=" + player.getUsername());
        player.message("Make sure to ::claimvote after you've voted for all the sites!");
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
