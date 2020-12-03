package com.fury.game.system.communication.commands.impl.owner;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.loaders.npc.MobSpawns;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.region.Region;

import java.util.regex.Pattern;

public class ReloadRegionNpcsCommand implements Command {

    @Override
    public Pattern pattern() {
        return null;
    }

    @Override
    public String prefix() {
        return "reload region npcs";
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public void process(Player player, String... values) {
        Region region = player.getRegion();
        region.setLoadMapStage(0);
        for (Mob mob : region.getNpcs(player.getZ())) {
            if (mob != null && !mob.isFamiliar())
                GameWorld.getMobs().remove(mob);
        }
        region.setLoadedNPCSpawns(false);
        MobSpawns.loadRegion(player.getRegionId());
        GameWorld.getRegions().load(player.getRegionId());
        player.message("Region " + player.getRegionId() + " npcs reloaded.");
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.OWNER);
    }
}
