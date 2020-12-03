package com.fury.game.container.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.types.NeverStackContainer;
import com.fury.core.model.item.Item;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.map.Position;

public class BeastOfBurden extends NeverStackContainer {

    public static final int INVENTORY_INTERFACE_ID = 2700;

    public static int INTERFACE_ID = 2702;

    public boolean canDeposit() {
        return canDeposit;
    }

    private boolean canDeposit = false;

    public BeastOfBurden open() {
        player.getPacketSender().sendInterfaceSet(INVENTORY_INTERFACE_ID, 3321);
        refresh();
        return this;
    }


    public BeastOfBurden(Player player, boolean deposit, int capacity) {
        super(player, capacity);
        this.canDeposit = deposit;
    }

    @Override
    public String getFullMessage() {
        return "Your familiar cannot hold any more items.";
    }

    @Override
    public void refresh() {
        player.getPacketSender().sendItemContainer(this, 2702);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }

    public void take() {
        if (!player.getInventory().hasRoom()) {
            player.getInventory().full();
            return;
        }

        player.animate(827);

        for(Item item : getItems())
            if(item != null)
                move(indexOf(item), player.getInventory(), player.getInventory().getFreeIndex());

        refresh();
        player.getInventory().refresh();
    }

    public void drop(Position position) {
        if (getItemTotal() > 0) {
            for (Item item : getItems())
                if(item != null)
                    FloorItemManager.addGroundItem(item, position, player);
            player.message("Your familiar has dropped its carried items on the floor.");
        }
    }

    public boolean contains(Item...items) {
        for(Item item : items) {
            if(contains(item))
                return true;
        }
        return false;
    }
}
