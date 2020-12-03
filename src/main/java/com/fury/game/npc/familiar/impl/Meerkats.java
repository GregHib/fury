package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class Meerkats extends Familiar {
    public Meerkats(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public String getSpecialName() {
        return "Fetch Casket";
    }

    @Override
    public String getSpecialDescription() {
        return "Dig's for a coordinate, compass, or clue.";
    }

    @Override
    public int getStoreSize() {
        return 0;
    }

    @Override
    public int getSpecialAttackEnergy() {
        return 12;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ENTITY;
    }

    @Override
    public boolean submitSpecial(Object object) {
        Player player = (Player) object;
        animate(14314);
        player.graphic(1409);
        return true;
    }
}
