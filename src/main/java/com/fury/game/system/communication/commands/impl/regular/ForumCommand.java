package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class ForumCommand implements Command {

    static Pattern pattern = Pattern.compile("\\b(forum|forums|form|forms)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "for";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getPacketSender().sendUrl(GameSettings.WEBSITE + "/forums/");
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
