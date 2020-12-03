package com.fury.game.container.action.impl;

import com.fury.game.container.impl.BeastOfBurden;
import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToRemoveFromBob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class BeastOfBurdenActions extends ContainerActions {

    public static final void withdraw(Player player, int id, int slot, int amount, boolean all) {
        if (player.getInterfaceId() != BeastOfBurden.INVENTORY_INTERFACE_ID || player.getFamiliar() == null || player.getFamiliar().getBeastOfBurden() == null)
            return;

        if (!player.getFamiliar().getBeastOfBurden().validate(id, slot))
            return;

        Item item = player.getFamiliar().getBeastOfBurden().get(slot);

        int currentAmount = player.getFamiliar().getBeastOfBurden().getAmount(item);

        if(currentAmount < amount || all)
            amount = currentAmount;

        for (int i = 0; i < amount; i++)
            player.getFamiliar().getBeastOfBurden().move(item, player.getInventory());

        player.getFamiliar().getBeastOfBurden().refresh();
        player.getInventory().refresh();
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        withdraw(player, id, slot, 1, false);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        withdraw(player, id, slot, 5, false);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        withdraw(player, id, slot, 10, false);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        withdraw(player, id, slot, 0, true);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        player.setInputHandling(new EnterAmountToRemoveFromBob(id, slot));
        player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
    }
}
