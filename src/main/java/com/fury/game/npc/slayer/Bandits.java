package com.fury.game.npc.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.bosses.godwars.saradomin.SaradominMinion;
import com.fury.game.npc.bosses.godwars.zamorak.ZamorakMinion;
import com.fury.game.world.map.Position;

import java.util.ArrayList;

public class Bandits extends Mob {
    public Bandits(int id, Position position, boolean spawned) {
        super(id, position, spawned);
        setForceAggressive(true);
    }

    @Override
    public ArrayList<Figure> getPossibleTargets() {
        ArrayList<Figure> targets = super.getPossibleTargets();
        ArrayList<Figure> targetsCleaned = new ArrayList<>();
        for (Figure t : targets) {
            if (!t.isPlayer() || (!ZamorakMinion.Companion.hasGodItem((Player) t) && !SaradominMinion.Companion.hasGodItem((Player) t)))
                continue;
            targetsCleaned.add(t);
        }
        return targetsCleaned;
    }

    @Override
    public void setTarget(Figure figure) {
        if (figure.isPlayer() && (ZamorakMinion.Companion.hasGodItem((Player) figure) || SaradominMinion.Companion.hasGodItem((Player) figure)))
            forceChat(ZamorakMinion.Companion.hasGodItem((Player) figure) ? "Prepare to suffer, Zamorakian scum!" : "Time to die, Saradominist filth!");
        super.setTarget(figure);
    }
}
