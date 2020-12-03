package com.fury.game.entity.character.player.actions;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.Flag;

public class SitChair extends Action {

    private int chair;
    private Position originalTile;
    private Position chairTile;
    private boolean tped;

    public SitChair(Player player, int chair, GameObject object) {
        this.chair = chair;
        this.originalTile = player.copyPosition();
        chairTile = object.copyPosition();
        Position face = player.copyPosition();
        if (object.getType() == 10) {
            if (object.getDirection() == 0)
                face.add(0, -1, 0);
            else if (object.getDirection() == 1)
                face.add(-1, 0, 0);
            else if (object.getDirection() == 2)
                face.add(0, 1, 0);
            else if (object.getDirection() == 3)
                face.add(1, 0, 0);

        } else if (object.getType() == 11) {
            if (object.getDirection() == 0)
                face.add(-1, -1, 0);
            else if (object.getDirection() == 1)
                face.add(-1, 1, 0);
            else if (object.getDirection() == 2)
                face.add(1, 1, 0);
            else if (object.getDirection() == 3)
                face.add(1, -1, 0);
        }
        player.getDirection().face(face);
    }

    @Override
    public boolean start(Player player) {
        setActionDelay(player, 1);
        return true;
    }

    @Override
    public boolean process(Player player) {
        return true;
    }

    @Override
    public int processWithDelay(Player player) {
        if (!tped) {
            player.moveTo(chairTile);
            tped = true;
        }

        player.getCharacterAnimations().setStandingAnimation(chair > 12 ? HouseConstants.THRONE_EMOTES[chair - 12] : chair > 6 ? HouseConstants.BENCH_EMOTES[chair - 6] : HouseConstants.CHAIR_EMOTES[chair]);
        player.getUpdateFlags().add(Flag.APPEARANCE);
        return 0;
    }

    @Override
    public void stop(final Player player) {
        player.getMovement().lock(1);
        player.moveTo(originalTile);

        player.getCharacterAnimations().reset();
        WeaponAnimations.update(player);
        player.getUpdateFlags().add(Flag.APPEARANCE);
    }
}
