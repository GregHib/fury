package com.fury.game.container;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.logs.PlayerLogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Container {

    protected Item[] items;
    protected int capacity;
    public Player player;

    public int capacity() {
        return capacity;
    }

    public Container(Player player, Container container) {
        this.player = player;
        this.capacity = container.capacity();
        this.items = container.getItems().clone();
    }

    public Container(Player player, int capacity) {
        this.player = player;
        this.capacity = capacity;
        this.items = new Item[capacity];
    }

    public void extend(int capacity) {
        this.capacity = capacity;
        this.items =  Arrays.copyOf(this.items, capacity);
    }

    /**
     * Commands
     */
    public boolean add(Item item) {
        if(item == null)
            return false;

        if(!hasRoom()) {
            full();
            return false;
        }

        items[getFreeIndex()] = item.copy();
        return true;
    }

    public boolean set(Item item, int index) {
        if(item == null || indexOutOfBounds(index))
            return false;

        items[index] = item.copy();
        return true;
    }

    public boolean move(int from, Container container, int to) {
        if(indexOutOfBounds(from) || container.indexOutOfBounds(to))
            return false;

        Item item = get(from);
        return delete(from) && container.set(item, to);
    }

    /*
        One index to another
     */
    public boolean swap(int index1, int index2) {
        if(indexOutOfBounds(index1) || indexOutOfBounds(index2))
            return false;

        Item item1 = get(index1);
        Item item2 = get(index2);
        if(delete(index1) && delete(index2))
            return (item2 != null ? set(item2, index1) : true) && (item1 != null ? set(item1, index2) : true);
        return false;
    }

    /*
        one container to another
     */
    public boolean swap(int index1, Container container, int index2) {
        if(indexOutOfBounds(index1) || container.indexOutOfBounds(index2))
            return false;

        Item item1 = get(index1);
        Item item2 = container.get(index2);
        boolean delete = delete(index1) && container.delete(index2);
        if(delete) {
            boolean set1 = item2 == null ? delete(index1) : set(item2, index1);
            boolean set2 = item1 == null ? delete(index2) : container.set(item1, index2);
            return set1 && set2;
        }
        return false;
    }

    public boolean delete(int index) {
        if(indexOutOfBounds(index))
            return false;

        items[index] = null;
        return true;
    }

    public void clear() {
        for(int index = 0; index < capacity; index++)
            items[index] = null;
    }

    public final boolean indexOutOfBounds(int index) {
        return index < 0 || index >= capacity;
    }

    public boolean exists(int index) {
        if(indexOutOfBounds(index))
            return false;

        return items[index] != null;
    }

    public Item get(int index) {
        if(indexOutOfBounds(index))
            return null;
        return items[index] == null ? null : items[index].copy();
    }

    public boolean validate(int id, int slot) {
        Item item = get(slot);
        if(item != null && item.getId() == id)
            return true;
        return false;
    }

    public void refresh() {
    }

    public boolean setItems(Item[] items) {
        if(items == null)
            return false;

        //Check but don't want to return otherwise all items will be lost.
        if(items.length > capacity) {
            System.err.println("Container too small, player: " + player.getUsername() + " " + this.getClass().getSimpleName() + " capacity: " + capacity);
            StringBuilder builder = new StringBuilder();
            for(int i = capacity; i < items.length; i++)
                builder.append(items[i] + ", ");
            System.err.println("Item's lost: " + builder.toString());
            PlayerLogs.log(player.getUsername(), "Items lost from " + this.getClass().getSimpleName() + " container: " + builder.toString());
        }

        System.arraycopy(items, 0, this.items, 0, Math.min(items.length, capacity));
        return true;
    }

    protected String getFullMessage() {
        return "Item container has reached maximum capacity.";
    }

    public final void full() {
        if(player != null) {
            String message = getFullMessage();
            if (message != null)
                player.message(message);
        }
    }

    public boolean hasRoom() {
        int index = getFreeIndex();
        if(index == -1)
            return false;//Container full
        return true;
    }

    public final int getSpaces() {
        int count = 0;
        for (int i = 0; i < capacity; i++)
            if (items[i] == null)
                count++;
        return count;
    }

    public final int getItemTotal() {
        int count = 0;
        for (int i = 0; i < capacity; i++)
            if (items[i] != null)
                count++;
        return count;
    }

    public final int getFreeIndex() {
        for (int i = 0; i < capacity; i++)
            if (items[i] == null)
                return i;
        return -1;
    }

    public final Item[] getItems() {
        return items;
    }

    public final Item[] getNotNullItems() {
        List<Item> notNull = new ArrayList<>();
        for(int i = 0; i < capacity; i++)
            if(items[i] != null)
                notNull.add(items[i]);
        return notNull.toArray(new Item[notNull.size()]);
    }

    public final int[] getIds() {
        int[] ids = new int[capacity];
        for(int i = 0; i < capacity; i++)
            ids[i] = items[i] == null ? 0 : items[i].getId();

        return ids;
    }

    public int indexOf(Item item) {
        if(item == null)
            return -1;

        for (int i = 0; i < capacity; i++)
            if (items[i] != null && items[i].isEqual(item))
                return i;
        return -1;
    }

    public final boolean contains(Item item) {
        if(item == null)
            return false;

        int index = indexOf(item);

        if(index == -1)//Container doesn't have item
            return false;

        return true;
    }

    public int getAmount(Item item) {
        int total = 0;
        for(Item slot : items)
            if(slot != null && slot.isEqual(item))
                total += slot.getAmount();
        return total;
    }
}
