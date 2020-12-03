package com.fury.game.container.types;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class AlwaysStackContainer extends StackContainer {

    public AlwaysStackContainer(Player player, int capacity) {
        super(player, capacity);
    }

    @Override
    public boolean add(Item item) {
        return stack(item);
    }
}
