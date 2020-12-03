package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class Dreadfowl extends Familiar {
    public Dreadfowl(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public String getSpecialName() {
        return "Strike";
    }

    @Override
    public String getSpecialDescription() {
        return "A long-ranged, magic based attack.";
    }

    @Override
    public int getStoreSize() {
        return 0;
    }

    @Override
    public int getSpecialAttackEnergy() {
        return 3;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }

    @Override
    public boolean submitSpecial(Object object) {
        Player player = (Player) object;
        player.animate(5387);
        player.graphic(1318);
        return true;
    }
}
