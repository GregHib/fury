package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;

import java.util.regex.Pattern;

public class DisableYellCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "disable yell";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        GameSettings.YELL_ACTIVE = false;
        player.message("Yell has been disabled globally.");
        GameWorld.sendStaffBroadcast(player.getUsername() + " has globally disabled yell.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
