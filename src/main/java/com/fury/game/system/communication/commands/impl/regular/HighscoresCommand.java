package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class HighscoresCommand implements Command {

    private static Pattern pattern = Pattern.compile("\\b(highscores|hiscores)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "hi";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getPacketSender().sendUrl(GameSettings.WEBSITE + "/highscores/");
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
