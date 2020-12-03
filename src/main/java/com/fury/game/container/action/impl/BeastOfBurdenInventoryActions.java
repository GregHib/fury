package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.container.impl.BeastOfBurden;
import com.fury.game.content.dialogue.input.impl.EnterAmountToStore;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;

public class BeastOfBurdenInventoryActions extends ContainerActions {

    public static void deposit(Player player, int id, int slot, int amount, boolean all) {
        if(player.getInterfaceId() != BeastOfBurden.INVENTORY_INTERFACE_ID || player.getFamiliar() == null || player.getFamiliar().getBeastOfBurden() == null)
            return;
        if(!player.getInventory().validate(id, slot))
            return;
        Item item = player.getInventory().get(slot);

        Familiar familiar = player.getFamiliar();

        if(!familiar.canDeposit(item))
            return;

        int currentAmount = player.getInventory().getAmount(item);
        if(currentAmount < amount || all)
            amount = currentAmount;

        boolean success = false;
        for(int i = 0; i < amount; i++)
            success |= player.getInventory().move(item, player.getFamiliar().getBeastOfBurden());

        if(!success)
            player.getFamiliar().getBeastOfBurden().full();

        player.getInventory().refresh();
        player.getFamiliar().getBeastOfBurden().refresh();
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 1, false);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 5, false);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 10, false);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 0, true);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if(player.getInterfaceId() != BeastOfBurden.INVENTORY_INTERFACE_ID || player.getFamiliar() == null || player.getFamiliar().getBeastOfBurden() == null)
            return;
        if(!player.getInventory().validate(id, slot))
            return;
        if (new Item(id).getDefinition().isStackable()) {
            player.message("You cannot store stackable items.");
            return;
        }
        player.setInputHandling(new EnterAmountToStore(id, slot));
        player.getPacketSender().sendEnterAmountPrompt("How many would you like to store?");
    }
}
