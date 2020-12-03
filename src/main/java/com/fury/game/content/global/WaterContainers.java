package com.fury.game.content.global;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 12/07/2016.
 */
public enum WaterContainers {
    POTION(null, new Item(229)),
    VIAL_OF_WATER(new Item(227), new Item(229)),
    POT_OF_FLOWER(new Item(1933), new Item(1931)),
    BOWL_OF_WATER(new Item(1921), new Item(1923)),
    BUCKET_OF_WATER(new Item(1929), new Item(1925)),
    BUCKET_OF_MILK(new Item(1927), new Item(1925)),
    JUG_OF_WATER(new Item(1937), new Item(1935)),
    DUNGEONEERING_VIAL_OF_WATER(new Item(17492), new Item(17490)),
    BURNT_PIE(new Item(2329), new Item(2313), true),
    COMPOST(new Item(6032), new Item(1925)),
    NEEM_OIL(new Item(22444, Revision.PRE_RS3), new Item(1935)),
    SUPERCOMPOST(new Item(6034), new Item(1925));
    private Item empty, full;
    private boolean firstClick;

    WaterContainers(Item full, Item empty) {
        this.full = full;
        this.empty = empty;
        this.firstClick = false;
    }

    WaterContainers(Item full, Item empty, boolean firstClick) {
        this.empty = empty;
        this.full = full;
        this.firstClick = firstClick;
    }

    public Item getEmpty() {
        return empty;
    }

    public Item getFull() {
        return full;
    }

    public boolean isFirstClick() { return firstClick; }

    public void setFull(Item full) {
        this.full = full;
    }

    public static WaterContainers forContainer(Item full, boolean first) {
        for (WaterContainers waterContainers : WaterContainers.values()) {
            if(waterContainers.full == null)
                continue;

            if(waterContainers.full.isEqual(full) && waterContainers.firstClick == first)
                return waterContainers;
        }
        return null;
    }


    public static boolean empty(Player player, Item full, boolean first) {
        String name = full.getDefinition().getName().toLowerCase();

        WaterContainers waterContainers = forContainer(full, first);
        if(name.contains(" (") && (name.contains("potion") || name.contains("brew") || name.contains("restore") ||
                name.contains("super") || name.contains("anti") || name.contains("extreme") || name.contains("overload")
                || name.contains("renewal"))) {
            waterContainers = WaterContainers.POTION;
            waterContainers.setFull(full);
        }
        if(waterContainers == null)
            return false;

        if (!player.getInventory().contains(waterContainers.getFull()))
            return false;
        player.getInventory().delete(waterContainers.full);
        player.getInventory().add(waterContainers.empty);

        if (name.contains(" ("))
            name = name.substring(0, name.indexOf(" ("));
        player.message("You empty the " + name + ".", true);
        return true;
    }
}
