package com.fury.game.content.skill.free.dungeoneering;

import com.fury.game.container.types.NeverStackContainer;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 05/07/2016.
 */
public class BindedItems extends NeverStackContainer {

    public BindedItems(Player player, int capacity) {
        super(player, capacity);
    }

    @Override
    public String getFullMessage() {
        return "A currently bound item must be destroyed before another item may be bound.";
    }
}
