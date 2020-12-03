package com.fury.game.system.communication.commands.impl.owner;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class RunesCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "runes";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        for (int i = 554; i <= 566; i++)
            player.getInventory().add(new Item(i, 20000));
        player.getInventory().add(new Item(9075, 20000));
        player.getInventory().add(new Item(21773, 20000, Revision.PRE_RS3));
        player.getInventory().refresh();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
