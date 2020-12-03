package com.fury.game.system.communication.commands.impl.helper;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;

import java.util.regex.Pattern;

public class RemindVoteCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "remindvote";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        GameWorld.sendBroadcast(FontUtils.imageTags(535) + " Remember to collect rewards by using the " + FontUtils.add("::vote", 0x800000) + " command every 12 hours!", 0x008fb2);
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isStaff())
            return true;
        return false;
    }
}
