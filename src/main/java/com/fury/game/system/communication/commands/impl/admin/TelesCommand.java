package com.fury.game.system.communication.commands.impl.admin;

import com.fury.game.content.global.ListWidget;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.TeleportCoords;
import com.fury.game.system.communication.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TelesCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "teles";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        List<String> locations = new ArrayList<>();
        for(TeleportCoords coord : TeleportCoords.values())
            locations.add(coord.getName());
        ListWidget.display(player, "Teleports List", "::tele [name]", locations.toArray(new String[locations.size()]));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
