package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class PotatoCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "potato";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(player.hasItem(5733))
            player.message("You already have a potato.");
        else
            player.getInventory().add(new Item(5733));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
