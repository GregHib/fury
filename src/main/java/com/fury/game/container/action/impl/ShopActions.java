package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToBuyFromShop;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class ShopActions extends ContainerActions {

    private int getSlot(Player player, int widget, int slot) {
        return widget == DungeonConstants.TOOLS_ITEM_CHILD_ID ? player.getShop().getDungeoneeringSplit() + slot : slot;
    }

    public static void buy(Player player, int id, int slot, int amount) {
        if(player.getShop() == null)
            return;

        if(!player.getShop().validate(id, slot))
            return;


        Item item = player.getShop().get(slot);
        item.setAmount(amount);

        if(!player.getControllerManager().canBuyShopItem(player, item))
            return;

        player.getShop().setPlayer(player);
        player.getShop().switchItem(player.getInventory(), item, slot, false, true);
    }


    @Override
    public void first(Player player, int widget, int id, int slot) {
        if(player.getShop() == null)
            return;

        if(!player.getShop().validate(id, widget == 605 ? slot + 72 : slot))
            return;

        player.getShop().checkBuyValue(player, getSlot(player, widget, slot));
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        buy(player, id, getSlot(player, widget, slot), 1);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        buy(player, id, getSlot(player, widget, slot), 5);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        buy(player, id, getSlot(player, widget, slot), 10);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (player.isBanking())
            return;
        if (player.isShopping()) {
            player.setInputHandling(new EnterAmountToBuyFromShop(id, getSlot(player, widget, slot)));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
            player.getShop().setPlayer(player);
        }
    }
}
