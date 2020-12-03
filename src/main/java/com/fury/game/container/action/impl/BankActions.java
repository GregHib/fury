package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToRemoveFromBank;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class BankActions extends ContainerActions {

    public static boolean withdrawAmount(Player player, int id, int slot, int amount, boolean all, boolean butOne) {
        if (!player.getBank().banking() || player.isCommandViewing())
            return false;

        int tab = player.getBank().getCurrentTab();

        //Search cancelling
        if(player.getBank().isSearching() && !player.getBank().getSearch().hasQuery()) {
            player.getBank().getSearch().stop();
            return false;
        }

        //Main page tabs
        if(tab == 0 && !player.getBank().isSearching() && player.getBank().getTabCount() > 1 && slot > player.getBank().tab().getItemTotal()) {
            int[] info = player.getBank().isolateTab(slot);
            if (info != null) {
                tab = info[0];
                slot = info[1];
            }
        }

        //Search withdrawing
        if(player.getBank().isSearching()) {
            Item[] results = player.getBank().getSearch().getResults();
            if(slot < 0 || slot >= results.length)
                return false;
            Item item = results[slot];
            if(!player.getBank().contains(item))//safety check
                return false;
            tab = player.getBank().getTabIndex(item);
            slot = player.getBank().tab(tab).indexOf(item);
        }

        //Withdraw
        if(!player.getBank().tab(tab).validate(id, slot))
            return false;

        Item item = player.getBank().tab(tab).get(slot);
        if(!all)
            item.setAmount(item.getAmount() < amount ? item.getAmount() : amount);
        else if(all && butOne)
            item.setAmount(item.getAmount() - 1);

        if(item.getAmount() > 0) {
            player.getBank().tab(tab).move(item, player.getInventory());
            player.getBank().checkCollapse(tab);
        }
        player.getBank().refresh();
        player.getInventory().refresh();
        return true;
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        withdrawAmount(player, id, slot, 1, false, false);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        withdrawAmount(player, id, slot, 5, false, false);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        withdrawAmount(player, id, slot, 10, false, false);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        withdrawAmount(player, id, slot, player.getBank().getWithdrawAmount(), false, false);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (!player.getBank().banking())
            return;

        player.setInputHandling(new EnterAmountToRemoveFromBank(id, slot));
        player.getPacketSender().sendEnterAmountPrompt("Enter the number of items you wish to withdraw:");
    }

    @Override
    public void sixth(Player player, int widget, int id, int slot) {
        withdrawAmount(player, id, slot, 0, true, false);
    }

    @Override
    public void seventh(Player player, int widget, int id, int slot) {
        withdrawAmount(player, id, slot, 0, true, true);
    }

    @Override
    public void eighth(Player player, int widget, int id, int slot) {
        if (!player.getBank().banking() || player.isCommandViewing())
            return;

        int tab = player.getBank().getCurrentTab();
        if(tab == 0 && player.getBank().getTabCount() > 1 && slot > player.getBank().tab().getItemTotal()) {
            int[] info = player.getBank().isolateTab(slot);
            if (info != null) {
                tab = info[0];
                slot = info[1];
            }
        }
        if(!player.getBank().tab(tab).validate(id, slot))
            return;
        Item item = player.getBank().tab(tab).get(slot);
        boolean remove = player.getBank().getPlaceHolders().contains(item);
        if(remove) {
            player.getBank().getPlaceHolders().remove(item);
            if(!player.getBank().isAlwaysPlaceholder() && item.getAmount() == 0) {
                player.getBank().tab(tab).delete(slot);
                player.getBank().checkCollapse(tab);
                player.getBank().refresh();
            }
        } else
            player.getBank().getPlaceHolders().add(item);
        player.message("Place holder " + (remove ? "removed" : "added") + " for item: " + item.getDefinition().getName());
    }
}
