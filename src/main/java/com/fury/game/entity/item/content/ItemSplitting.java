package com.fury.game.entity.item.content;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public enum ItemSplitting {
    ABYSSAL_VINE_WHIP(new Item(21371), new Item[] {new Item(4151), new Item(21369)}),
    BANDOS_GODSWORD(new Item(11696), new Item[] {new Item(11704), new Item(11690)}),
    ARMADYL_GODSWORD(new Item(11694), new Item[] {new Item(11702), new Item(11690)}),
    SARADOMIN_GODSWORD(new Item(11698), new Item[] {new Item(11706), new Item(11690)}),
    ZAMORAK_GODSWORD(new Item(11700), new Item[] {new Item(11708), new Item(11690)})
    ;

    Item itemRemoved;
    Item[] itemsAdded;

    ItemSplitting(Item itemRemoved, Item[] itemsAdded) {
        this.itemRemoved = itemRemoved;
        this.itemsAdded = itemsAdded;
    }

    public static boolean handleItemSplit(Player player, Item item) {
        for(ItemSplitting split : ItemSplitting.values()) {
            if(item.isEqual(split.itemRemoved)) {

                if(player.getInventory().getSpaces() < split.itemsAdded.length - 1) {
                    player.getInventory().full();
                    return true;
                }

                player.getInventory().delete(item);
                player.getInventory().add(split.itemsAdded);
                return true;
            }
        }
        return false;
    }
}
