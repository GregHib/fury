package com.fury.game.system.communication.commands.impl.dev;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class MemoryCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "memory";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        player.message("Heap usage: " + Misc.insertCommasToNumber("" + used + "") + " bytes!");
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            return true;
        return false;
    }
}
