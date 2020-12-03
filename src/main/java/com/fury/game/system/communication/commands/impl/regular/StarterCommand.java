package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.mysql.impl.DonationStore;

import java.util.regex.Pattern;

public class StarterCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "starter";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getPacketSender().sendUrl(GameSettings.WEBSITE + "/forums/topic/543-getting-started-updated/");

    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
