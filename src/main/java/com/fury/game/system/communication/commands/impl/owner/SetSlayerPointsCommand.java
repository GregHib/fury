package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class SetSlayerPointsCommand implements Command {

    static Pattern pattern = Pattern.compile("^set\\sslayer\\spoints\\s(.*)\\s(\\d+)$");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "set slayer points ";
    }

    @Override
    public String format() {
        return "set slayer points [name] [amount]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        int amount = Integer.valueOf(values[2]);

        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.getPoints().set(Points.SLAYER, amount);
        player.message("Set " + target.getUsername() + "'s slayer points to: " + amount + ".");
        target.getPointsHandler().refreshPanel();
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
