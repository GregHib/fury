package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class ThreadCommand implements Command {

    static Pattern pattern = Pattern.compile("\\bthread\\s(\\d+)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "thread ";
    }

    @Override
    public String format() {
        return "thread [thread id]";
    }

    @Override
    public void process(Player player, String... values) {
        int thread = Integer.parseInt(values[1]);
        player.getPacketSender().sendUrl(GameSettings.WEBSITE + "/forums/index.php?app=forums&module=forums&controller=topic&id=" + thread);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
