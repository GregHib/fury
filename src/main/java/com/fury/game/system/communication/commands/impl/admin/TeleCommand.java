package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.TeleportCoords;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.map.Position;

import java.util.regex.Pattern;

public class TeleCommand implements Command {

    static Pattern pattern = Pattern.compile("^(?:tele|tp)\\s(?:(\\d+)(?:,|\\s|,\\s|\\s,)(\\d+)(?:(?:,|\\s|,\\s|\\s,)(\\d+))?|(.+))$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return null;
    }

    @Override
    public String format() {
        return "tele [x] [y] (z) - or - tele [location name]";
    }

    @Override
    public void process(Player player, String... values) {
        if(values[4] == null) {
            int x = Integer.valueOf(values[1]), y = Integer.valueOf(values[2]);
            int z = values[3] != null ? Integer.valueOf(values[3]) : player.getZ();
            Position position = new Position(x, y, z);
            player.moveTo(position);
            player.message("Teleporting to " + position.toString());
        } else {
            Position position = TeleportCoords.get(values[4]);
            if (position != null)
                player.moveTo(position);
            else
                player.message("Could not find location '" + values[4] +"'.");
        }
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
