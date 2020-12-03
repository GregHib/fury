package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToRemoveFromTrade;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class TradeActions extends ContainerActions {

    public static void remove(Player player, int id, int slot, int amount, boolean all) {
        if(!player.getTrade().inTrade())
            return;

        if(!player.getTrade().validate(id, slot))
            return;

        Item item = player.getTrade().get(slot);
        int currentAmount = player.getTrade().getAmount(item);

        if(all)
            item.setAmount(currentAmount);
        else
            item.setAmount(amount > currentAmount ? currentAmount : amount);
        player.getTrade().move(item, player.getInventory());
        player.getTrade().refresh();
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        remove(player, id, slot, 1, false);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        remove(player, id, slot, 5, false);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        remove(player, id, slot, 10, false);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        remove(player, id, slot, 0, true);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (player.getTrade().inTrade()) {
            player.setInputHandling(new EnterAmountToRemoveFromTrade(id, slot));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
        }
    }
}
