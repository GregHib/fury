package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToBank;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class BankInventoryActions extends ContainerActions {

    private void deposit(Player player, int id, int slot, int amount) {
        if (!player.getBank().banking() || player.isCommandViewing())
            return;
        if (player.getInventory().validate(id, slot)) {
            Item item = player.getInventory().get(slot);
            int currentAmount = player.getInventory().getAmount(item);
            item.setAmount(currentAmount < amount ? currentAmount : amount);
            player.getBank().deposit(item, player.getInventory());
            player.getBank().refresh();
            player.getInventory().refresh();
        }
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 1);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 5);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, 10);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        deposit(player, id, slot, player.getBank().getWithdrawAmount());
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (player.isBanking()) {
            player.setInputHandling(new EnterAmountToBank(id, slot));
            player.getPacketSender().sendEnterAmountPrompt("Enter the number of items you wish to deposit:");
        }
    }

    @Override
    public void sixth(Player player, int widget, int id, int slot) {
        if (!player.getBank().banking() || player.isCommandViewing())
            return;

        if (player.getInventory().validate(id, slot)) {
            Item item = player.getInventory().get(slot);
            int amount = player.getInventory().getAmount(item);
            item.setAmount(amount);
            player.getBank().deposit(item, player.getInventory());
            player.getBank().refresh();
            player.getInventory().refresh();
        }
    }
}
