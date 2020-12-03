package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.map.Position;

import java.util.regex.Pattern;

public class DuelCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "duel";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        TeleportHandler.teleportPlayer(player, new Position(3365, 3269), TeleportType.RING_TELE);
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
