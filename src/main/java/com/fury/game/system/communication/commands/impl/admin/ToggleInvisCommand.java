package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.update.flag.Flag;

import java.util.regex.Pattern;

public class ToggleInvisCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "toggleinvis";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        if(player.getTransformation() != null)
            player.resetTransformation();
        else
            player.setTransformation(8254);
        player.getUpdateFlags().add(Flag.APPEARANCE);
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR))
            return true;
        return false;
    }
}
