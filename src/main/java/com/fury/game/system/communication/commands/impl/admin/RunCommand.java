package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class RunCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "run";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(player.isInWilderness()) {
            player.message("You can't restore run in the wilderness!");
            return;
        }
        player.getSettings().set(Settings.RUN_ENERGY, 100);
        player.getPacketSender().sendRunEnergy(100);
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
