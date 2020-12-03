package com.fury.game.system.communication.commands.impl.dev;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.bank.BankTab;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class BulkItemCommand implements Command {
    static Pattern pattern = Pattern.compile("^bitem\\s(\\d+)(?:\\s(\\S+))?(?:\\s([0-3]))?$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "bitem ";
    }

    @Override
    public String format() {
        return "bulkitem [startId] [endId] (revision 0-3)";
    }

    @Override
    public void process(Player player, String... values) {
        if (values[1] == null || values[2] == null)
            return;

        BankTab tab = player.getBank().tab();

        int startId = Integer.parseInt(values[1]);
        int endId = Integer.parseInt(values[2]);

        int difference = endId - startId;
        if(difference > tab.capacity()) {
            player.message("The difference was ["+difference+"] when it must be below ["+tab.capacity()+"]");
            return;
        }

        for(int i = startId; i <= endId; i++) {
            if(!tab.hasRoom()) {
                player.message("Last item spawned was " + (i - 1));
                break;
            }
            Item item = new Item(i, 1);
            tab.add(item);
        }
        tab.refresh();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.DEVELOPER);
    }
}