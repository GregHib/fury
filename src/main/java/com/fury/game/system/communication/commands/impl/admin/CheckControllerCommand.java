package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.regex.Pattern;

public class CheckControllerCommand implements Command {

    private static Pattern pattern = Pattern.compile("^check\\scontroller\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "check controller ";
    }

    @Override
    public String format() {
        return "check controller [username]";
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

        player.message(target.getUsername() + "'s current controller is: " + (target.getControllerManager().getController() == null ? "null" : target.getControllerManager().getController().getClass().getSimpleName()));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
