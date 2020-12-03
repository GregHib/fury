package com.fury.game.container.impl.bank;

import com.fury.game.container.Container;
import com.fury.game.container.impl.Inventory;
import com.fury.game.container.types.AlwaysStackContainer;
import com.fury.game.container.types.StackContainer;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class BankTab extends AlwaysStackContainer {

    Bank bank;

    public BankTab(Player player, int capacity, Bank bank) {
        super(player, capacity);
        this.bank = bank;
    }

    @Override
    public boolean add(Item item) {
        if (!contains(item) && bank != null && !bank.contains(item) && bank.isFull()) {
            full();
            return false;
        }
        return super.add(item);
    }

    @Override
    public boolean set(Item item, int index) {
        if (item == null || indexOutOfBounds(index))
            return false;

        //Placeholders
        if (bank != null && (bank.getPlaceHolders().contains(item) || bank.alwaysPlaceholder)) {
            items[index] = item.getAmount() >= 0 ? item.copy() : null;
        } else {
            items[index] = item.getAmount() > 0 ? item.copy() : null;
        }
        return true;
    }

    @Override
    public boolean move(int from, Container container, int to) {
        if (indexOutOfBounds(from) || container.indexOutOfBounds(to))
            return false;

        Item item = get(from);

        //Withdrawing as notes
        if (container instanceof Inventory) {
            if (bank != null && bank.withdrawNotes)
                item.getDefinition().toNote();
            item = new Item(item.getId(), item);
        }

        return delete(from) && container.set(item, to);
    }

    @Override
    public boolean move(Item item, StackContainer to) {

        if(item == null || to == null) {
            System.err.println("Error moving bank item: " + item + " " + to);
            return false;
        }

        //Withdrawing as notes
        if (to instanceof Inventory && bank != null && bank.withdrawNotes) {
            Item note = null;
            if (item.getDefinition().noteId != -1) {
                note = new Item(item.getDefinition().noteId, item);
            }

            if (note != null) {
                item.setDefinition(note);

                boolean added = to.add(note);
                if (added)
                    delete(item);
                return added;
            } else {
                player.message("That item cannot be withdrawn as a note.");
            }
        }

        //Placeholders
        if(to instanceof BankTab) {
            boolean added = to.add(item);
            if (added)
                delete(indexOf(item));//Actually delete's instead of making a placeholder
            return added;
        } else
            return super.move(item, to);
    }

    @Override
    public boolean delete(Item item) {
        if(item == null || !contains(item))
            return false;

        if(item.getAmount() == 0)
            return false;

        return super.delete(item);
    }

    public boolean insert(int index1, BankTab container, int index2) {
        if (indexOutOfBounds(index1) || indexOutOfBounds(index2))
            return false;

        container.insert(index1, index2);
        return true;
    }

    public boolean insert(int index1, int index2) {
        if (indexOutOfBounds(index1) || indexOutOfBounds(index2))
            return false;

        Item[] newItems = new Item[capacity];

        boolean reverse = index1 > index2;
        int first = reverse ? index2 : index1;
        int second = reverse ? index1 : index2;

        //Unmoved items
        System.arraycopy(items, 0, newItems, 0, first);

        if (!reverse)
            System.arraycopy(items, index1 + 1, newItems, index1, index2 - index1);

        //The moved item
        System.arraycopy(items, index1, newItems, index2, 1);

        if (reverse)
            System.arraycopy(items, index2, newItems, index2 + 1, index1 - index2);

        //Everything after
        System.arraycopy(items, second + 1, newItems, second + 1, (capacity - 1) - second);

        items = newItems;
        return true;
    }

    public Item get(Item item) {
        if (!contains(item))
            return null;
        return get(indexOf(item));
    }

    public boolean add(Item... items) {
        boolean success = false;
        for (Item item : items)
            success |= add(item);
        return success;
    }

    @Override
    public void refresh() {
        if (bank != null)
            bank.refresh();
    }

    @Override
    public String getFullMessage() {
        return "Your bank is full.";
    }
}
