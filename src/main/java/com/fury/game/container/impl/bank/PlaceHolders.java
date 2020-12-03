package com.fury.game.container.impl.bank;

import com.fury.core.model.item.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlaceHolders {

    private List<Item> placeholders = new ArrayList<>();

    public boolean contains(Item item) {
        for(Item held : placeholders)
            if(held.isEqual(item))
                return true;
        return false;
    }

    public void add(Item item) {
        Item copy = item.copy();
        copy.setAmount(-1);
        placeholders.add(copy);
    }

    public void remove(Item item) {
        Iterator<Item> iterator = placeholders.iterator();

        while (iterator.hasNext()) {
            Item held = iterator.next();

            if (held.isEqual(item))
                iterator.remove();
        }
    }

    public Item[] getItems() {
        return placeholders.toArray(new Item[placeholders.size()]);
    }

    public void addAll(Item[] items) {
        for(Item item : items)
            placeholders.add(item);
    }
}
