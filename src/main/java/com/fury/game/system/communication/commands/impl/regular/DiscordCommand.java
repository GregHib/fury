package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class DiscordCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "discord";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.getPacketSender().sendUrl(GameSettings.DISCORD);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
