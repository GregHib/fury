package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.skill.free.smithing.Smithing;
import com.fury.game.content.skill.free.smithing.SmithingData;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class SmithingActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {
        Item item = new Item(id);
        int barsRequired = SmithingData.getBarAmount(item);
        Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
        int x = 1;
        if (x > (player.getInventory().getAmount(bar) / barsRequired))
            x = (player.getInventory().getAmount(bar) / barsRequired);
        Smithing.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired), new Item(item, SmithingData.getItemAmount(item)), x);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        Item item = new Item(id);
        int barsRequired = SmithingData.getBarAmount(item);
        Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
        int x = 5;
        if (x > (player.getInventory().getAmount(bar) / barsRequired))
            x = (player.getInventory().getAmount(bar) / barsRequired);
        Smithing.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired), new Item(item, SmithingData.getItemAmount(item)), x);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        Item item = new Item(id);
        int barsRequired = SmithingData.getBarAmount(item);
        Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
        int x = 10;
        if (x > (player.getInventory().getAmount(bar) / barsRequired))
            x = (player.getInventory().getAmount(bar) / barsRequired);
        Smithing.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired), new Item(item, SmithingData.getItemAmount(item)), x);
    }
}
