package com.fury.game.system.communication.commands.impl.regular;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;

import java.util.regex.Pattern;

public class HelpCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "help";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if (player.getTimers().getYell().elapsed(30000)) {

            GameWorld.sendStaffBroadcast(FontUtils.imageTags(535) + " " + FontUtils.add("[TICKET SYSTEM]", 0xff0066) + " " + player.getUsername() + " has requested help. Please help them!", 0x6600ff);
            player.getTimers().getYell().reset();
            player.message("Your help request has been received. Please be patient.", 0x663300);
        } else {
            player.message("");
            player.message("You need to wait 30 seconds before using this again.", 0x663300);
            player.message("If it's an emergency, please private message a staff member directly instead.", 0x663300);
        }
    }

    @Override
    public boolean rights(Player player) {
        return true;
    }
}
