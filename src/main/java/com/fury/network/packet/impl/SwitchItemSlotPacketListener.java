package com.fury.network.packet.impl;

import com.fury.game.container.impl.Inventory;
import com.fury.game.container.impl.bank.Bank;
import com.fury.core.model.item.Item;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * This packet listener is called when an item is dragged onto another slot.
 *
 * @author relex lawl
 */

public class SwitchItemSlotPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)
            return;
        int widget = packet.readLEShortA();
        boolean insert = packet.readByteC() == 1;
        int fromSlot = packet.readLEShortA();
        int toSlot = packet.readLEShort();

        switch (widget) {
            case Inventory.INTERFACE_ID:
            case Bank.INVENTORY_INTERFACE_ID:
                if (player.getInventory().exists(fromSlot))
                    player.getInventory().swap(fromSlot, toSlot);
                break;
            case Bank.INTERFACE_ID:
                if (fromSlot == toSlot)
                    return;
                if (player.getBank().isSearching())
                    return;

                int currentTab = player.getBank().getCurrentTab();
                int tab1 = currentTab;
                int tab2 = currentTab;
                int slot1 = fromSlot;
                int slot2 = toSlot;

                if (player.getBank().getCurrentTab() == 0 && player.getBank().getTabCount() > 1) {
                    int total = player.getBank().tab().getItemTotal();
                    if (toSlot > total) {
                        int[] info = player.getBank().isolateTab(toSlot);
                        if (info != null) {
                            tab2 = info[0];
                            slot2 = info[1];
                        }
                    }

                    if (fromSlot > total) {
                        int[] info = player.getBank().isolateTab(fromSlot);
                        if (info != null) {
                            tab1 = info[0];
                            slot1 = info[1];
                        }
                    }
                }

                if (player.getBank().tab(tab1).exists(slot1)) {
                    if (player.getBank().swapMode()) {
                        player.getBank().swap(tab1, slot1, tab2, slot2);
                    } else {
                        player.getBank().insert(tab1, slot1, tab2, slot2);
                    }
                    player.getBank().checkCollapse(tab1);
                    player.getBank().refresh();
                }
                break;
            case Bank.BANK_TAB_INTERFACE_ID:
                if (player.getBank().isSearching())
                    return;

                currentTab = player.getBank().getCurrentTab();
                tab1 = currentTab;
                slot1 = fromSlot;

                if (player.getBank().getCurrentTab() == 0 && player.getBank().getTabCount() > 1) {
                    int total = player.getBank().tab().getItemTotal();
                    if (fromSlot > total) {
                        int[] info = player.getBank().isolateTab(fromSlot);
                        if (info != null) {
                            tab1 = info[0];
                            slot1 = info[1];
                        }
                    }
                }

                if (player.getBank().tab(tab1).exists(slot1)) {
                    Item item = player.getBank().tab(tab1).get(slot1);
                    if(item != null) {
                        if (player.getBank().tabExists(toSlot)) {
                            if(tab1 != toSlot) {
                                player.getBank().tab(tab1).move(item, player.getBank().tab(toSlot));
                                player.getBank().checkCollapse(tab1);
                            }
                        } else if(player.getBank().tab(tab1).getItemTotal() != 1)
                            player.getBank().addTab(tab1, slot1, toSlot);
                        player.getBank().refresh();
                    }
                }
                break;
        }
    }
}
