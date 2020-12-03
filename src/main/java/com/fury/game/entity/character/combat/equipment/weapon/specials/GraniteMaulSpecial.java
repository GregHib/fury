package com.fury.game.entity.character.combat.equipment.weapon.specials;


import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.equipment.weapon.SpecialAttack;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class GraniteMaulSpecial implements SpecialAttack {
    @Override
    public int[] getIdentifiers() {
        return new int[] { 4153 };
    }

    @Override
    public int drainAmount() {
        return 50;
    }

    @Override
    public double multiplier() {
        return 1.1;
    }

    @Override
    public void execute(Player player, Figure target, boolean main) {
        player.animate(1667);
        player.perform(new Graphic(340, 0, 96 << 16));
//        delayNormalHit(weapon, fightType, getMeleeHit(player, getRandomMaxHit(player, weapon, fightType, false, true, 1.1, true)));
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}
