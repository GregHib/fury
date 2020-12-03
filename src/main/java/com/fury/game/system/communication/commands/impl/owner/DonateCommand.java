package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class DonateCommand implements Command {

    static Pattern pattern = Pattern.compile("^donate\\s(.*)\\s(-?\\d+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "donate ";
    }

    @Override
    public String format() {
        return "donate [username] [amount]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        int amount = Integer.parseInt(values[2]);

        target.getPoints().add(Points.DONATED, amount);
        player.message(target.getUsername() + " donated $" + amount);
        target.message("You donate $" + amount);
        target.getPacketSender().sendRights();
        player.getPointsHandler().refreshPanel();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
