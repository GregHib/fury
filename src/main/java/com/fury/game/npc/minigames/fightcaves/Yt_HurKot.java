package com.fury.game.npc.minigames.fightcaves;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Utils;

import java.util.ArrayList;

public class Yt_HurKot extends FightCavesMob {
    
    private TzTok_Jad jad;
    private int nextHealTick;
    
    public Yt_HurKot(int id, Position position, TzTok_Jad tzTokJad) {
        super(id, position);
        this.jad = tzTokJad;
        setForceAggressive(false);
    }

    @Override
    public void processNpc() {
        super.processNpc();
        if (jad == null || jad.isDead()) {
            deregister();
            return;
        }
        if (!getCombat().isInCombat()) {
            getMovement().addWalkStepsInteract(jad.getX(), jad.getY(), -1, jad.getSizeX(), jad.getSizeY(), true);
            if (jad.getHealth().getHitpoints() == jad.getMaxConstitution() || !Utils.isOnRange(this, jad, 6)) {
                return;
            }
            nextHealTick++;
            if (nextHealTick % 2 == 0) {
                jad.perform(new Graphic(444, 0, 300));
                animate(9254);
            }
            if (nextHealTick % 5 == 0) {// Approx 3 seconds
                getDirection().setInteracting(jad);
                jad.getHealth().heal((int) (jad.getMaxConstitution() * .03));
            }
        }
    }

    @Override
    public ArrayList<Figure> getPossibleTargets() {
        return super.getPossibleTargets(false, true);
    }
}
