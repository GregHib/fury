package com.fury.game.system.communication.commands.impl.admin;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.region.Region;

import java.util.regex.Pattern;

public class RegionCommand implements Command {
    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "region";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        int regionX = player.getChunkX() - 6;
        int regionY = player.getChunkY() - 6;
        int regionId = ((regionX / 8) << 8) + (regionY / 8);
        int x = (regionId >> 8) << 6;
        int y = (regionId & 0xFF) << 6;
        Region region = player.getRegion();
        player.message("Region ID: " + player.getRegionId() + " " + Revision.getRevision(player.getRegionId()) + " " + region.getPlayerCount());
        player.message("RegionX: " + player.getChunkX() + " RegionY: " + player.getChunkY());
        player.message("CoordX: " + x + " CoordY: " + y);
        Position local = GameWorld.getRegions().get(player.getRegionId()).getLocalPosition(player);
        player.message("Local: " + local.getX() + ", " + local.getY() + " - " + player.getLocalX() + ", " + player.getLocalY());
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR);
    }
}
