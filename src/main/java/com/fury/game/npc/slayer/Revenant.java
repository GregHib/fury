package com.fury.game.npc.slayer;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

import java.util.ArrayList;

public class Revenant extends Mob {
    public Revenant(int id, Position position, boolean spawned) {
        super(id, position, spawned);
    }

    @Override
    public void spawn() {
        super.spawn();
        perform(new Animation(getSpawnAnimation()));
    }

    @Override
    public ArrayList<Figure> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
        int sizeX = getSizeX();
        int sizeY = getSizeY();
        int agroRatio = 2;
        ArrayList<Figure> possibleTarget = new ArrayList<>();
        for (Player player : GameWorld.getRegions().getLocalPlayers(this)) {
            if (player == null || player.getZ() != getZ() || player.isDead() || player.getFinished() || !player.getSettings().getBool(Settings.RUNNING) /*|| player.getAppearence().isHidden()*/ || !Misc.isOnRange(getX(), getY(), sizeX, sizeY, player.getX(), player.getY(), player.getSizeX(), player.getSizeY(), agroRatio) || !getCombat().clippedProjectile(player, false))
                continue;
            possibleTarget.add(player);
        }
        return possibleTarget;
    }

    private int getSpawnAnimation() {
        switch (getId()) {
            case 13465:
                return 7410;
            case 13466:
            case 13467:
            case 13468:
            case 13469:
                return 7447;
            case 13470:
            case 13471:
                return 7485;
            case 13472:
                return -1;
            case 13473:
                return 7426;
            case 13474:
                return 7403;
            case 13475:
                return 7457;
            case 13476:
                return 7464;
            case 13477:
                return 7478;
            case 13478:
                return 7416;
            case 13479:
                return 7471;
            case 13480:
                return 7440;
            case 13481:
            default:
                return -1;
        }
    }
}
