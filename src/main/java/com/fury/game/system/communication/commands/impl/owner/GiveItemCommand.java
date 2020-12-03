package com.fury.game.system.communication.commands.impl.owner;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class GiveItemCommand implements Command {

    private static Pattern pattern = Pattern.compile("^giveitem\\s(\\d+)(?:\\s(\\d+))?\\s(.*)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "giveitem ";
    }

    @Override
    public String format() {
        return "giveitem [item id] (amount) [player]";
    }

    @Override
    public void process(Player player, String... values) {
        int id = Integer.parseInt(values[1]);
        int amount = values[2] != null ? (int) Misc.stringToAmount(values[2]) : 1;
        String name = values[values.length - 1];
        Revision revision = player.getRevision();

        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        Item item = new Item(id, amount, revision);
        player.message("Gave " + amount + "x "+ item.getName() + " to " + name + ".");
        target.getInventory().add(item);
        target.getInventory().refresh();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
