package com.fury.game.system.communication.commands.impl.mod;

import com.fury.game.content.misc.objects.DwarfMultiCannon;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.World;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ResetDecayedCannonsCommand implements Command {

    private static Pattern pattern = Pattern.compile("^reset\\sdecayed\\scannons\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "reset decayed cannons ";
    }

    @Override
    public String format() {
        return "reset decayed cannons [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        PlayerLogs.log(player.getUsername(), "Reset " + target.getUsername() + "'s decayed cannons. " + Arrays.toString(player.getDecayedCannons().toArray(new DwarfMultiCannon.CannonType[player.getDecayedCannons().size()])));
        player.getDecayedCannons().clear();
        player.message(target.getUsername() + "'s decayed cannons were successfully reset.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
