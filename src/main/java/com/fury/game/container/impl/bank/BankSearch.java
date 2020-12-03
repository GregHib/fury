package com.fury.game.container.impl.bank;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.input.impl.EnterSyntaxToBankSearchFor;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.util.ArrayList;
import java.util.List;

public class BankSearch {

    public void setPlayer(Player player) {
        this.player = player;
    }

    Bank bank;
    Player player;
    String searchQuery;

    public BankSearch(Bank bank) {
        this.bank = bank;
    }

    public void stop() {
        player.getPacketSender().sendConfig(117, 0).sendString(5383, "Bank of " + GameSettings.NAME);
        player.getPacketSender().sendClientRightClickRemoval();
        searchQuery = null;
        bank.setSearching(false);
        player.setInputHandling(null);
        bank.refresh();
    }

    public void search(String string) {
        player.getPacketSender().sendInterfaceComponentScrollPosition(5385, 0);
        player.getPacketSender().sendClientRightClickRemoval();
        player.getPacketSender().sendString(5383, "Bank of " + GameSettings.NAME + "(search: '" + string + "')");

        bank.setCurrentTab(0);
        searchQuery = string;

        refresh();

        bank.refresh();
    }

    public boolean handleButton(int id) {
        if(id == 22004) {
            if (!bank.isSearching()) {
                bank.setSearching(true);
                player.getPacketSender().sendConfig(117, 1);
                if (player.getInputHandling() == null) {
                    player.setInputHandling(new EnterSyntaxToBankSearchFor());
                    player.getPacketSender().sendEnterInputPrompt("Enter the name of the item you wish to search for:");
                }
            } else
               stop();
            return true;
        }
        return false;
    }

    public void refresh() {
        if (hasQuery()) {
            Item[] items = getResults();
            player.getPacketSender().sendItemContainer(items, 5382);
            player.getPacketSender().sendInterfaceComponentScrollMax(5385, (int) Math.round(items.length * 4.5));
        }
    }

    public Item[] getResults() {
        List<Item> items = new ArrayList<>();
        if(hasQuery()) {
            for (int i = 0; i < bank.getTabCount(); i++)
                for (Item item : bank.tab(i).getItems())
                    if (item != null && item.getDefinition().name != null)
                        if (item.getDefinition().name.toLowerCase().contains(searchQuery))
                            items.add(item);
        }
        return items.toArray(new Item[items.size()]);
    }

    public boolean hasQuery() {
        return searchQuery != null && searchQuery.length() > 0;
    }

    public String getQuery() {
        return searchQuery;
    }
}
