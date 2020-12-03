package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.mysql.VoteClaim;

import java.util.regex.Pattern;

public class ClaimVoteCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "claimvote";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(!player.getTimers().getClickDelay().elapsed(10000)) {
            player.message("You can only do this every 10 seconds.");
            return;
        }
        VoteClaim.claim(player);
        player.getTimers().getClickDelay().reset();
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
