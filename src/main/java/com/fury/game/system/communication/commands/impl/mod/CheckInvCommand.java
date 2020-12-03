package com.fury.game.system.communication.commands.impl.mod;

import com.fury.game.container.impl.Inventory;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class CheckInvCommand implements Command {

    private static Pattern pattern = Pattern.compile("^check\\sinv\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "check inv ";
    }

    @Override
    public String format() {
        return "check inv [username]";
    }

    @Override
    public void process(Player player, String... values) {
        if (player.getInterfaceId() > 0) {
            player.message("Please close the interface you have open before opening another one.");
            return;
        }

        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("'" + name + "' is not currently online.");

            if(!player.getRights().isOrHigher(PlayerRights.OWNER))
                return;

            target = SearchUtils.getPlayerFromName(name);
        }

        if(target == null) {
            player.message("Error loading that players file.");
            return;
        }

        if(player.getRightsData().previewInventory == null)
            player.getRightsData().previewInventory = new Inventory(player, target.getInventory());

        player.setCommandViewing(true);
        player.message("Move or interact with inventory to revert back to your own.");
        player.getRightsData().previewInventory.refresh();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
