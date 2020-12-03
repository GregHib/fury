package com.fury.game.system.communication.commands.impl.dev;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.MessageType;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class DebugCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "debug";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        GameSettings.DEBUG = !GameSettings.DEBUG;
        player.getPacketSender().sendMessage(MessageType.PLAYER_ALERT, "Debug " + (GameSettings.DEBUG ? "activated" : "deactivated") + ".");
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            return true;
        return false;
    }
}
