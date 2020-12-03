package com.fury.game.system.communication.commands.impl.dev;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.map.Position;

import java.util.regex.Pattern;

public class WalkCommand implements Command {

    static Pattern pattern = Pattern.compile("\\bwalk\\s(-?\\d+)\\s(-?\\d+)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "walk ";
    }

    @Override
    public String format() {
        return "walk (x) (y)";
    }

    @Override
    public void process(Player player, String... values) {
        int x = Integer.valueOf(values[1]);
        int y = Integer.valueOf(values[2]);

        Position position = player.copyPosition();
        position.add(x, y);
        player.setForceMovement(new ForceMovement(player, 1, position, 4, ForceMovement.NORTH));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.DEVELOPER);
    }
}
