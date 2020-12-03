package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.mysql.impl.DonationStore;

import java.util.regex.Pattern;

public class ReferCommand implements Command {

    private static Pattern pattern = Pattern.compile("^refer\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "claim";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        DonationStore.checkDonation(player);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
