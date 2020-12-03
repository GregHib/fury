package com.fury.game.system.communication.commands.impl.mod;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class CheckPouchCommand implements Command {

    private static Pattern pattern = Pattern.compile("^check\\spouch\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "check pouch ";
    }

    @Override
    public String format() {
        return "check pouch [username]";
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

        player.message(target.getUsername() + " has " + Misc.insertCommasToNumber(target.getMoneyPouch().getTotal()) + "gp in their pouch.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
