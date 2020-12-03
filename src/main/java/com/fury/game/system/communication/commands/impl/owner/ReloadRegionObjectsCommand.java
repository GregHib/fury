package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.region.Region;

import java.util.regex.Pattern;

public class ReloadRegionObjectsCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "reload region objects";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        Region region = player.getRegion();
        region.setLoadMapStage(0);
        if(region.getSpawnedObjects() != null && region.getSpawnedObjects().size() > 0) {
            for (GameObject object : region.getSpawnedObjects()) {
                if (object != null)
                    ObjectManager.removeObject(object);
            }
        }
        region.setLoadedObjectSpawns(false);
        GameWorld.getRegions().load(player.getRegionId());
        player.message("Region " + player.getRegionId() + " objects reloaded.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
