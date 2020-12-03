package com.fury.game.entity.character.combat.equipment.weapon;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public interface SpecialAttack {

    int[] getIdentifiers();

    int drainAmount();

    double multiplier();

    void execute(Player player, Figure target, boolean main);

    boolean isInstant();
}
