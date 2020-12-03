package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToSellToShop;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class ShopInventoryActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {

        if(player.getShop() == null)
            return;

        if(!player.getInventory().validate(id, slot))
            return;

        player.getShop().checkSellValue(player, slot);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        if (player.isShopping())
            player.getShop().sellItem(player, slot, 1);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        if (player.isShopping())
            player.getShop().sellItem(player, slot, 5);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        if (player.isShopping())
            player.getShop().sellItem(player, slot, 10);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (player.isBanking())
            return;
        if (player.isShopping()) {
            player.setInputHandling(new EnterAmountToSellToShop(id, slot));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to sell?");
            player.getShop().setPlayer(player);
        }
    }

    @Override
    public void sixth(Player player, int widget, int id, int slot) {
        if (player.isShopping())
            player.getShop().sellItem(player, slot, player.getInventory().getAmount(new Item(id)));
    }
}
