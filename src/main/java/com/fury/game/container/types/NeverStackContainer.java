package com.fury.game.container.types;

import com.fury.game.container.Container;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class NeverStackContainer extends StackContainer {

    public NeverStackContainer(Player player, int capacity) {
        super(player, capacity);
    }

    @Override
    public boolean add(Item item) {
        if(failsChecks(item))
            return false;

        if(!hasRoom()) {
            //full();
            return false;
        }

        items[getFreeIndex()] = item.copy();
        return true;
    }

    @Override
    public boolean move(int from, Container container, int to) {
        if(indexOutOfBounds(from) || container.indexOutOfBounds(to))
            return false;

        Item item = get(from);
        if(failsChecks(item))
            return false;

        return delete(from) && container.set(item, to);
    }

    @Override
    public boolean swap(int index1, Container container, int index2) {
        if(indexOutOfBounds(index1) || container.indexOutOfBounds(index2))
            return false;

        Item item1 = get(index1);
        Item item2 = container.get(index2);

        if(failsChecks(item2))//Only needs to check 2 as 1 is already in container.
            return false;
        boolean delete = delete(index1) && container.delete(index2);
        if(delete)
            return set(item2, index1) && container.set(item1, index2);
        return false;
    }

    @Override
    public boolean set(Item item, int index) {
        if(failsChecks(item) || indexOutOfBounds(index))
            return false;

        items[index] = item.getAmount() > 0 ? item.copy() : null;
        return true;
    }

    private boolean failsChecks(Item item) {
        return item == null || item.getAmount() > 1 || item.getDefinition().stackable;
    }

    @Override
    public boolean move(Item item, StackContainer to) {
        boolean added = to.add(item);
        if(added)
            delete(indexOf(item));
        return added;
    }
}
