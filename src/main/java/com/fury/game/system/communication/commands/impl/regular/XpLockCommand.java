package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class XpLockCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "xplock";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        player.setExperienceLocked(!player.experienceLocked());
        String type = player.experienceLocked() ? "locked" : "unlocked";
        player.message("Your experience is now " + type + ".");
        PlayerPanel.refreshPanel(player);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
