package com.fury.game.system.communication.commands.impl.dev;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.misc.items.random.impl.imps.MysteryBoxTimedGen;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class TimedBoxCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "timedbox";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(player.hasItem(18768)) {
            player.message("You already have a timed box.");
            return;
        }
        Item item = new Item(18768, 1);
        player.getInventory().add(item);
        MysteryBoxTimedGen.newTimerFor(player, item);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.DEVELOPER);
    }
}
