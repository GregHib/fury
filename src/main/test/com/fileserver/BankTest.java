package com.fileserver;

import com.fury.core.model.item.Item;

public class BankTest {
    static Item[] inventory = new Item[90];
    public static void main(String[] args) {
        for(int i = 0; i < 90; i++)
            inventory[i] = new Item(i, 1);

        int index = 60;
        extend(index);

        for(int i = index - 5; i < index + 15; i++) {
            System.out.println(i + " " + inventory[i].getId());
        }
        System.out.println(inventory.length);
    }

    public static void extend(int index) {
        int amount = -10;
        Item[] newItems = new Item[inventory.length + amount];
        System.arraycopy(inventory, 0, newItems, 0, index + 1);
        if(amount > 0)
            for(int i = 0; i < amount; i++)
                newItems[index + 1 + i] = new Item(0, 0, null);
        System.arraycopy(inventory, index + 1, newItems, index + 1 + amount, inventory.length - (index + 1));
        inventory = newItems;
    }
}
