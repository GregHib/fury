package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.content.global.ListWidget;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.files.plugin.loader.CommandLoader;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class CommandsCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "commands";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        String[] commands = CommandLoader.getCommands(player);
        if(commands != null)
            ListWidget.display(player, "Commands", "Name - Format" + (player.getRights().isStaff() ? " [Required] (Optional)" : ""), commands);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
