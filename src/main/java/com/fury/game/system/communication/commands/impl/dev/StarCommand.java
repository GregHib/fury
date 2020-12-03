package com.fury.game.system.communication.commands.impl.dev;

import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;

import java.util.regex.Pattern;

public class StarCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "star";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        ShootingStar.init();
        if(player != null && ShootingStar.getLocation() != null && ShootingStar.getLocation().getPosition() != null)
            player.moveTo(ShootingStar.getLocation().getPosition());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
