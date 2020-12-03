package com.fury.game.container.impl;

import com.fury.game.container.types.StackContainer;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class StakeContainer extends StackContainer {

    public StakeContainer(Player player) {
        super(player, 28);
    }
}
