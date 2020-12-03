package com.fury.game.system.communication.commands.impl.admin;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.build.DynamicRegion;

import java.util.regex.Pattern;

public class TeleToMeCommand implements Command {

    private static Pattern pattern = Pattern.compile("^teletome\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "teletome ";
    }

    @Override
    public String format() {
        return "teletome [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Could not find player '" + name + "' online.");
            return;
        }

        boolean canTele = TeleportHandler.checkReqs(player, target.copyPosition()) && !(GameWorld.getRegions().get(player.getRegionId()) instanceof DynamicRegion);
        if (canTele) {
            TeleportHandler.teleportPlayer(target, player.copyPosition(), TeleportType.NORMAL);
            player.message("Teleporting player to you: " + target.getUsername() + "");
            target.message("You're being teleported to " + player.getUsername() + "...");
        } else
            player.message("You can not teleport that player at the moment. Maybe you or they are in a minigame?");
        PlayerLogs.log(player.getUsername(), "Teleported player " + name + " to them " + player.copyPosition());

    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
