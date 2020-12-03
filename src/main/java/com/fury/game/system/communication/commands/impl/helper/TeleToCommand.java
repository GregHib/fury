package com.fury.game.system.communication.commands.impl.helper;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.build.DynamicRegion;

import java.util.regex.Pattern;

public class TeleToCommand implements Command {

    static Pattern pattern = Pattern.compile("^teleto\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "teleto ";
    }

    @Override
    public String format() {
        return "teleto [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Cannot find player '" + name + "' online...");
            return;
        }

        boolean tele = TeleportHandler.checkReqs(player, target.copyPosition()) && !(GameWorld.getRegions().get(target.getRegionId()) instanceof DynamicRegion);
        if (tele) {
            TeleportHandler.teleportPlayer(player, target.copyPosition(), TeleportType.NORMAL);
            player.message("Teleporting to player: " + target.getUsername() + "");
        } else
            player.message("This player is at a place you cannot teleport to e.g a minigame.");

    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isStaff();
    }
}
