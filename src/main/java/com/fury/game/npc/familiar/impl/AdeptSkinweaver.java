package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class AdeptSkinweaver extends Familiar {

    public AdeptSkinweaver(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public String getSpecialName() {
        return "Glimmer of Light";
    }

    @Override
    public String getSpecialDescription() {
        return "Restores 90 hitpoints.";
    }

    @Override
    public int getStoreSize() {
        return 0;
    }

    @Override
    public int getSpecialAttackEnergy() {
        return 13;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.CLICK;
    }

    @Override
    public boolean submitSpecial(Object object) {
        Player player = (Player) object;
        player.getHealth().heal(90);//Increases by 10 each time
        getOwner().graphic(1300);
        getOwner().animate(7660);
        return true;
    }
}
