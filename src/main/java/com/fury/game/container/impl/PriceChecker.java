package com.fury.game.container.impl;

import com.fury.game.container.types.StackContainer;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class PriceChecker extends StackContainer {

    public static final int INTERFACE_ID = 42000, INVENTORY_INTERFACE_ID = 2100, CONTAINER_ID = 18246;
    private boolean open;

    public PriceChecker(Player player) {
        super(player, 28);
    }

    public void open() {
        player.getMovement().reset();
        player.message("Note: When selling an item to a store, it loses 15% of its original value!", true);
        player.message("Prices shown in the price-checker are the original values!", true);
        refresh();
        open = true;
    }
    
    @Override
    public void refresh() {
        sort();
        player.getPacketSender().sendItemContainer(this, CONTAINER_ID);
        player.getPacketSender().sendInterfaceSet(INTERFACE_ID, 3321);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
        player.getPacketSender().sendString(18437, getItemTotal() == 0 ? "Click on items in your inventory to check their values." : "");

        int index = 0;
        for(Item item : getItems()) {
            boolean slotOccupied = item != null && item.getId() > 0;
            if(slotOccupied) {
                if(item.getDefinition().isStackable() && item.getAmount() != 1) {
                    int itemAmount = item.getAmount();
                    long totalPrice = (long) item.getDefinition().getValue() * itemAmount;
                    int frame = getFrame(index);
                    player.getPacketSender().sendString(frame, Misc.insertCommasToNumber(item.getDefinitions().getValue()) + " x" + Misc.insertCommasToNumber(itemAmount));
                    player.getPacketSender().sendString(frame + 1, "= " + Misc.insertCommasToNumber(totalPrice));
                } else {
                    player.getPacketSender().sendString(getFrame(index), Misc.insertCommasToNumber(Integer.toString(item.getDefinitions().getValue())));
                }
            } else {
                player.getPacketSender().sendString(getFrame(index), "");
                player.getPacketSender().sendString(getFrame(index) + 1, "");
            }
            player.getPacketSender().sendString(18351, Misc.insertCommasToNumber(Long.toString(calculateTotalWealth())));
            index++;
        }
    }

    @Override
    public String getFullMessage() {
        return "The price checker cannot hold any more items.";
    }

    public long calculateTotalWealth() {
        long total = 0;
        for(Item item: getItems())
            if(item != null)
                total += (long) item.getDefinition().getValue() * item.getAmount();
        return total;
    }


    private static final int[] frames = {
            18353, 18356, 18359, 18362, 18365, 18368,
            18371, 18374, 18377, 18380, 18383, 18386,
            18389, 18392, 18395, 18398, 18401, 18404,
            18407, 18410, 18413, 18416, 18419, 18422,
            18425, 18428, 18431, 18434
    };

    public static int getFrame(int slot) {
        return frames[slot];
    }

    public boolean isOpen() {
        return open;
    }

    public void exit() {
        for(Item item : getItems()) {
            if(item != null)
                move(item, player.getInventory());
        }
        player.getInventory().refresh();
        open = false;
        player.getPacketSender().sendInterfaceRemoval();
    }

    public void withdrawAll() {
        for(Item item : getItems())
            if(item != null)
                move(item, player.getInventory());
        player.getInventory().refresh();
        refresh();
    }

    public void depositInventory() {
        for(Item item : player.getInventory().getItems())
            if(item != null && item.tradeable())
                player.getInventory().move(item, this);
        player.getInventory().refresh();
        refresh();
    }
}
