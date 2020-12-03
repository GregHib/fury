package com.fury.game.npc.minigames.fightcaves;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.map.Position;

import java.util.ArrayList;
import java.util.Collection;

public class FightCavesMob extends Mob {

    public FightCavesMob(int id, Position position) {
        super(id, position, true);
        setNoDistanceCheck(true);
        setBlockDeflections(true);
    }

    @Override
    public ArrayList<Figure> getPossibleTargets() {
        ArrayList<Figure> possibleTarget = new ArrayList<>(1);
        Collection<Player> players = getRegion().getPlayers(getZ());
        for(Player player: players) {
            if(player == null || player.isDead() || player.getFinished() || !player.getSettings().getBool(Settings.RUNNING))
                continue;
            possibleTarget.add(player);
        }
        return possibleTarget;
    }
}
