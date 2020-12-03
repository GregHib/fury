package com.fury.game.system.communication.commands.impl.helper;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.build.DynamicRegion;

import java.util.regex.Pattern;

public class MoveHomeCommand implements Command {

    static Pattern pattern = Pattern.compile("^movehome\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "movehome ";
    }

    @Override
    public String format() {
        return "movehome [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String name = values[1];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("Cannot find player '" + name + "' online.");
            return;
        }

        boolean canTele = TeleportHandler.checkReqs(player, target.copyPosition()) && !(GameWorld.getRegions().get(player.getRegionId()) instanceof DynamicRegion);
        if (canTele) {
            TeleportHandler.teleportPlayer(target, GameSettings.DEFAULT_POSITION.copyPosition(), TeleportType.NORMAL);
            player.message("Teleporting player to home: " + target.getUsername() + "");
            target.message("You're being teleported home by " + player.getUsername() + "...");
        } else
            player.message("You can not teleport that player at the moment. Maybe you or they are in a minigame?");
    }

    @Override
    public boolean rights(Player player) {
        if(player.getRights().isStaff())
            return true;
        return false;
    }
}
