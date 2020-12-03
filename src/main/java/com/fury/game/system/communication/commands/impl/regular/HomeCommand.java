package com.fury.game.system.communication.commands.impl.regular;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class HomeCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "home";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copyPosition(), TeleportType.NORMAL);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
