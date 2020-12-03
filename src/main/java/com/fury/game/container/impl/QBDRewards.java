package com.fury.game.container.impl;

import com.fury.game.container.types.AlwaysStackContainer;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 22/03/2017.
 */
public class QBDRewards extends AlwaysStackContainer {

    public QBDRewards(Player player) {
        super(player, 15);
    }
}
