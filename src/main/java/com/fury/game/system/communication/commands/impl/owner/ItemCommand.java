package com.fury.game.system.communication.commands.impl.owner;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.system.communication.commands.Command;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class ItemCommand implements Command {

    static Pattern pattern = Pattern.compile("^item\\s(\\d+)(?:\\s(\\S+))?(?:\\s([0-3]))?$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "item ";
    }

    @Override
    public String format() {
        return "item [id] (amount) (revision 0-3)";
    }

    @Override
    public void process(Player player, String... values) {
        int id = Integer.parseInt(values[1]);
        if(values[2] != null && values[2].length() > String.valueOf(Integer.MAX_VALUE).length()) {
            player.message("Invalid number");
            return;
        }
        int amount = values[2] == null ? 1 : (int) Misc.stringToAmount(values[2]);
        Revision revision = values[3] == null ? Revision.RS2 : Revision.values()[Integer.parseInt(values[3])];

        Item item = new Item(id, amount, revision);
        player.getInventory().add(item);
        player.getInventory().refresh();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
