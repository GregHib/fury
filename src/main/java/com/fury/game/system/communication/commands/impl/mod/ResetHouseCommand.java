package com.fury.game.system.communication.commands.impl.mod;

import com.fury.game.content.skill.member.construction.House;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;

import java.util.regex.Pattern;

public class ResetHouseCommand implements Command {

    private static Pattern pattern = Pattern.compile("^reset\\shouse\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset house ";
    }

    @Override
    public String format() {
        return "reset house [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        target.getHouse().expelGuests();
        target.getHouse().leaveHouse(target, House.KICKED);
        target.getHouse().reset();
        player.message(target.getUsername() + "'s house was successfully reset.");
        PlayerLogs.log(player.getUsername(), "Reset " + target.getUsername() + "'s house.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
