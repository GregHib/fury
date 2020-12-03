package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.controller.impl.duel.DuelController;
import com.fury.game.content.dialogue.input.impl.EnterAmountToStake;
import com.fury.game.content.dialogue.input.impl.EnterAmountToTrade;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class TradeInventoryActions extends ContainerActions {

    public static void trade(Player player, int id, int slot, int amount, boolean all) {
        if (!player.getInventory().validate(id, slot))
            return;

        Item item = player.getInventory().get(slot);
        int currentAmount = player.getInventory().getAmount(item);

        if(!item.tradeable()) {
            player.message("This item cannot be traded.");
            return;
        }

        if (all)
            item.setAmount(currentAmount);
        else
            item.setAmount(amount > currentAmount ? currentAmount : amount);
        player.getInventory().move(item, player.getTrade());
        player.getTrade().tradeItem();
        player.getTrade().refresh();
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        if (player.getTrade().inTrade() && player.getInterfaceId() == 3323) {
            trade(player, id, slot, 1, false);
        } else if(player.getControllerManager().getController() instanceof DuelController){
            player.getDuelConfigurations().offerStake(player, slot, 1);
        }
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        if (player.getTrade().inTrade() && player.getInterfaceId() == 3323) {
            trade(player, id, slot, 5, false);
        } else if(player.getControllerManager().getController() instanceof DuelController){
            player.getDuelConfigurations().offerStake(player, slot, 5);
        }
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        if (player.getTrade().inTrade() && player.getInterfaceId() == 3323) {
            trade(player, id, slot, 10, false);
        } else if(player.getControllerManager().getController() instanceof DuelController){
            player.getDuelConfigurations().offerStake(player, slot, 10);
        }
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        if (player.getTrade().inTrade() && player.getInterfaceId() == 3323) {
            trade(player, id, slot, 0, true);
        } else if(player.getControllerManager().getController() instanceof DuelController){
            player.getDuelConfigurations().offerStake(player, slot, player.getInventory().getAmount(new Item(id)));
        }
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (player.getTrade().inTrade() && player.getInterfaceId() == 3323) {
            player.setInputHandling(new EnterAmountToTrade(id, slot));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to trade?");
        } else if(player.getControllerManager().getController() instanceof DuelController){
            player.setInputHandling(new EnterAmountToStake(id, slot));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to stake?");
        }
    }
}
