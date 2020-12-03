package com.fury.game.container.impl;

import com.fury.game.container.types.AlwaysStackContainer;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class GemBag extends AlwaysStackContainer {

    private static Item[] gems = new Item[] {
            new Item(1623),//Sapphire
            new Item(1621),//Emerald
            new Item(1619),//Ruby
            new Item(1617)//Diamond
    };

    public static int getIndex(int id) {
        for(int index = 0; index < gems.length; index++)
            if (id == gems[index].getId())
                return index;
        return -1;
    }

    public GemBag(Player player) {
        super(player, 4);
    }

    @Override
    public String getFullMessage() {
        return "Your gem bag is full.";
    }

    public void empty() {
        int spaces = player.getInventory().getSpaces();

        if(spaces <= 0) {
            player.getInventory().full();
            return;
        }

        int gemSpaces = getSpaces();

        if(gemSpaces == capacity()) {
            player.message("Your gem bag is empty.");
            return;
        }

        int total = spaces;
        for(Item gem : items) {
            if(gem == null)
                continue;
            for(int i = 0; i < gem.getAmount(); i++) {
                move(new Item(gem, 1), player.getInventory());
                total--;
                if(total <= 0) {
                    player.message("You withdraw as many gems as you can.");
                    return;
                }
            }
        }

        player.message("You withdraw all the gems.");
    }

    private int total() {
        int total = 0;
        for(Item gem : gems)
            total += getAmount(gem);
        return total;
    }

    public void fill() {
        boolean moved = false;
        for(int slot = 0; slot < player.getInventory().capacity(); slot++) {
            Item item = player.getInventory().get(slot);
            if(item == null)
                continue;
            if(!item.getDefinition().isNoted() && !item.getDefinition().isStackable()) {
                int index = getIndex(item.getId());

                if(index == -1)
                    continue;

                if(!store(item, slot, index))
                    return;
                moved = true;
            }
        }
        if(moved)
            player.message("You fill your bag with gems.");
        else
            player.message("You do not have any gems to store in your bag.");
    }

    public boolean store(Item item, int slot, int index) {
        if(indexOutOfBounds(index))
            return false;

        if (total() + 1 > 100) {
            full();
            return false;
        }

        if(items[index] == null) {
            items[index] = item;
        } else {
            if(!item.isEqual(items[index]))
                return false;

            int amount = items[index].getAmount();

            items[index].setAmount(amount + 1);
        }
        player.getInventory().delete(slot);
        return true;
    }

    public void check() {
        //"Your gem bag has 0 sapphires, 0 emeralds, 0 rubies and 0 diamonds."
        int[] amounts = new int[gems.length];
        for(int i = 0; i < gems.length; i++) {
            int index = getIndex(gems[i].getId());

            if(index == -1)
                continue;

            if(contains(gems[i]))
                amounts[i] = getAmount(gems[i]);
        }

        player.message("You gem bag has " + amounts[0] + " sapphires, " + amounts[1] + " emeralds, " + amounts[2] + " rubies and " + amounts[3] + " diamonds.");
    }

    public void withdraw() {
        empty();
    }
}
