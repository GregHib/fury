package com.fury.game.container.types;

import com.fury.game.container.Container;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class StackContainer extends Container {

    public StackContainer(Player player, StackContainer container) {
        super(player, container);
    }

    public StackContainer(Player player, int capacity) {
        super(player, capacity);
    }

    @Override
    public boolean add(Item item) {
        if(item == null)
            return false;

        if(isStackable(item)) {
            //Item exists in container
            return stack(item);
        } else {
            boolean success = false;
            if(item.getAmount() > 30) {
                System.out.println("Issue dropping: " + item.toString());
                return false;
            }
            Item copy = new Item(item, 1);
            for(int i = 0; i < item.getAmount(); i++)
                success |= super.add(copy);
            return success;
        }
    }

    @Override
    public boolean set(Item item, int index) {
        if(item == null || indexOutOfBounds(index))
            return false;

        items[index] = item.getAmount() > 0 ? item.copy() : null;
        return true;
    }

    public boolean deduct(Item item) {
        if(item == null || !contains(item))
            return false;

        if (!haveEnough(item))
            return false;

        int index = indexOf(item);
        int amount = items[index].getAmount();
        int newAmount = amount - item.getAmount();
        Item newItem = new Item(item, newAmount);
        return set(newItem, index);
    }

    public boolean delete(Item item) {
        if(item == null || !contains(item))
            return false;

        if(isStackable(item) || item.getAmount() == 1) {
            return deduct(item);
        } else {
            Item copy = new Item(item, 1);
            for(int i = 0; i < item.getAmount(); i++)
                if (!deduct(copy))
                    return false;
            return true;
        }
    }

    protected boolean isStackable(Item item) {
        return item.getDefinition().stackable;
    }

    private boolean haveEnough(Item item) {
        Item get = get(indexOf(item));
        if(get == null)
            return false;

        int amount = get.getAmount();
        if(amount < item.getAmount()) {
            return false;//Container has less than asked to remove
        } else {
            return true;//Container has more than or equal to the amount asked to remove
        }
    }

    public boolean stack(Item item) {
        boolean contains = contains(item);

        if(!contains && !hasRoom()) {
            full();
            return false;
        }

        int index = contains ? indexOf(item) : getFreeIndex();
        int amount = contains ? get(index).getAmount() : 0;
        long newAmount = amount + item.getAmount();

        if(newAmount < 0 || newAmount > Integer.MAX_VALUE) {
            full();
            return false;
        }

        if(contains)
            items[index].setAmount((int) newAmount);
        else
            items[index] = item.copy();
        return true;
    }

    public boolean move(Item item, StackContainer to) {
        if(!isStackable(item) && item.getAmount() > 0)
            if(to.getSpaces() > 0 && to.getSpaces() < item.getAmount())
                item.setAmount(to.getSpaces());

        boolean added = to.add(item);
        if(added)
            delete(item);
        return added;
    }

    public boolean swap(Item item1, StackContainer container, Item item2) {
        int index1 = indexOf(item1);
        int index2 = container.indexOf(item2);

        if(indexOutOfBounds(index1) || container.indexOutOfBounds(index2))
            return false;

        boolean delete = delete(index1) && container.delete(index2);
        if(delete) {
            boolean set1 = item2 == null ? delete(index1) : this instanceof StackContainer && contains(item2) && item2.getDefinition().isStackable() ? stack(item2) : set(item2, index1);
            boolean set2 = item1 == null ? delete(index2) : container.set(item1, index2);
            return set1 && set2;
        }
        return false;
    }

    public void sort() {
        Item[] newItems = new Item[capacity];
        int index = 0;
        for (int i = 0; i < capacity; i++)
            if (items[i] != null)
                newItems[index++] = items[i];
        items = newItems;
    }

    public void refresh() {
    }

}
