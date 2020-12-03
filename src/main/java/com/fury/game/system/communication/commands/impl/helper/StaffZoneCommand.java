package com.fury.game.system.communication.commands.impl.helper;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.map.Position;

import java.util.regex.Pattern;

public class StaffZoneCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "staffzone";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        TeleportHandler.teleportPlayer(player, new Position(2846, 5147), TeleportType.NORMAL);
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isStaff())
            return true;
        return false;
    }
}
