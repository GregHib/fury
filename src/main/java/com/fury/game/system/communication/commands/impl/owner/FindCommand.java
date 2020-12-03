package com.fury.game.system.communication.commands.impl.owner;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class FindCommand implements Command {

    static Pattern pattern = Pattern.compile("^find(r?)\\s(.*)$");//"^find(?:(?:(r?)\\s(\\D+))|(?:\\s(\\d+)(?:\\s([0-3]))?))$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "find";
    }


    @Override
    public String format() {
        return "find(r) [item name] - or - find [item id]";
    }

    @Override
    public void process(Player player, String... values) {
        boolean number = false;

        try {
            Integer.parseInt(values[2]);
            number = true;
        } catch (NumberFormatException e) {}

        if(!number) {
            boolean reversed = !values[1].isEmpty();
            String name = values[2];

            if (name.length() < 3) {
                player.message("An item search must use more than 3 characters");
                return;
            }

            player.message("Finding item id for item - " + name);
            boolean found = false;

            //TODO implement G.E search algorithm
            int total = Loader.getTotalItems(Revision.PRE_RS3);
            if (reversed) {
                for (int index = total - 1; index > 0; index--) {
                    ItemDefinition def = Loader.getItem(index, Revision.PRE_RS3);

                    if (matches(def, name)) {
                        if(!player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR))
                            player.message("Found item with name [" + def.getName().toLowerCase() + "] - id: " + index);
                        else
                            player.getPacketSender().sendConsoleMessage("Found item with name [" + def.getName().toLowerCase() + "] - id: " + index);
                        found = true;
                    }
                }
            } else {
                for (int index = 0; index < total; index++) {
                    ItemDefinition def = Loader.getItem(index, Revision.PRE_RS3);

                    if (matches(def, name)) {
                        if(!player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR))
                            player.message("Found item with name [" + def.getName().toLowerCase() + "] - id: " + index);
                        else
                            player.getPacketSender().sendConsoleMessage("Found item with name [" + def.getName().toLowerCase() + "] - id: " + index);
                        found = true;
                    }
                }
            }

            if (!found)
                if(!player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR))
                    player.message("No item with name [" + name + "] has been found!");
                else
                    player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
        } else {
            int item = Integer.parseInt(values[2]);
            Revision revision = Revision.RS2;
            ItemDefinition definition = Loader.getItem(item, revision);
            player.message("Found item with name [" + definition.getName().toLowerCase() + "] - id: " + item);
        }
    }

    private boolean matches(ItemDefinition def, String name) {
        return def != null && !def.noted && def.getName() != null && def.getName().toLowerCase().contains(name);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
