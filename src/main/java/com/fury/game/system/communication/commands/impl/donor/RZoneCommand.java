package com.fury.game.system.communication.commands.impl.donor;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.regex.Pattern;

public class RZoneCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "rzone";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        TeleportHandler.teleportPlayer(player, new Position(1055 + Misc.random(2), 5003 + Misc.random(2)), TeleportType.NORMAL);
    }

    @Override
    public boolean rights(Player player) {
        if(DonorStatus.isDonor(player,DonorStatus.RUBY_DONOR) || player.getRights().isStaff())
            return true;
        return false;
    }
}
