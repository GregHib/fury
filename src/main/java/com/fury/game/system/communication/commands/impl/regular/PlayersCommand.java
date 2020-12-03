package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;

import java.util.regex.Pattern;

public class PlayersCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "players";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
//        List<String> names = new ArrayList<>();
        int online = GameWorld.getPlayers().size();
        /*for (Player user : GameWorld.getPlayers())
            if(user != null)
                names.add(user.getUsername());*/

        int fake = (int) (online > 0 ? (Math.round(online * 1.2) + 5) : 0);
        player.message("There are currently " + fake + " players online.");
//        ListWidget.display(player, "Active Players" + (player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR) ? " (" + online + " online)" : ""), " (" + fake + " total) (" + (fake - online) + " inactive)", names.toArray(new String[names.size()]));
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
